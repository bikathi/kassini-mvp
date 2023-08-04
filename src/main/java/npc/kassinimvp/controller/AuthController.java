package npc.kassinimvp.controller;

import lombok.extern.slf4j.Slf4j;
import npc.kassinimvp.entity.AppUser;
import npc.kassinimvp.payload.request.LoginRequest;
import npc.kassinimvp.payload.request.SignupRequest;
import npc.kassinimvp.payload.response.GenericServiceResponse;
import npc.kassinimvp.payload.response.LoginResponse;
import npc.kassinimvp.payload.response.MessageResponse;
import npc.kassinimvp.security.jwt.JwtUtils;
import npc.kassinimvp.security.service.UserDetailsImpl;
import npc.kassinimvp.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AppUserService appUserService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${service.api.version}")
    private String apiVersion;

    @Value("${service.organization.name}")
    private String organizationName;

    @PostMapping("/signin")
    public ResponseEntity<?> signinUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String authToken = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
            "Successfully signed in user", HttpStatus.OK.value(), new LoginResponse(
                userDetails.getUsername(), userDetails.getEmail(), userDetails.getPhoneNumber(), authToken
        )));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        // ensure the provided email does not already exist
        if(appUserService.userExistsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Email is already in use", HttpStatus.BAD_REQUEST.value(), new MessageResponse("Email is already In Use")));
        }

        AppUser newUser = new AppUser(
            generateUserId(),
            signupRequest.getFirstName(),
            signupRequest.getLastName(),
            signupRequest.getEmail(),
            passwordEncoder.encode(signupRequest.getPassword()),
            signupRequest.getPhoneNumber()
        );

        appUserService.createNewUserAccount(newUser);

        return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
            "Successfully signed up user", HttpStatus.OK.value(), new MessageResponse("Successfully signed up user")));
    }

    private String generateUserId() {
        return UUID.randomUUID().toString()
            .replace("-", "").substring(0, 12);
    }
}
