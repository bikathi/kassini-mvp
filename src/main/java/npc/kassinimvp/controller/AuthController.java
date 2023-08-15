package npc.kassinimvp.controller;

import lombok.extern.slf4j.Slf4j;
import npc.kassinimvp.entity.Groups;
import npc.kassinimvp.entity.definitions.AppRoles;
import npc.kassinimvp.entity.AppUser;
import npc.kassinimvp.entity.definitions.Location;
import npc.kassinimvp.payload.request.LoginRequest;
import npc.kassinimvp.payload.request.SignupRequest;
import npc.kassinimvp.payload.response.GenericServiceResponse;
import npc.kassinimvp.payload.response.LoginResponse;
import npc.kassinimvp.payload.response.MessageResponse;
import npc.kassinimvp.security.jwt.JwtUtils;
import npc.kassinimvp.security.service.UserDetailsImpl;
import npc.kassinimvp.service.AppUserService;
import npc.kassinimvp.service.DestinationManagementService;
import npc.kassinimvp.service.GroupsService;
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

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DestinationManagementService destinationManagementService;

    @Autowired
    private GroupsService groupsService;

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
        Set<String> userRoles = new HashSet<>();
        userDetails.getAuthorities().forEach(grantedAuthority -> {
            if(Objects.equals(grantedAuthority.getAuthority(), "ROLE_VENDOR")) {
                userRoles.add("vendor");
            } else if(Objects.equals(grantedAuthority.getAuthority(), "ROLE_BUYER")) {
                userRoles.add("buyer");
            }
        });

        return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
            "Successfully signed in user", HttpStatus.OK.value(), new LoginResponse(
                userDetails.getUserId(), userDetails.getUsername(), userDetails.getBioName(), userDetails.getEmail(), userDetails.getPhoneNumber(), authToken, userDetails.getLocation(),
                userRoles
        )));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        // ensure the provided email does not already exist
        if(appUserService.userExistsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Email is already in use", HttpStatus.BAD_REQUEST.value(), new MessageResponse("Email is already In Use")));
        }

        // ensure that the provided bioName is not already in use
        if(appUserService.userExistsByBioName(signupRequest.getBioName())) {
            return ResponseEntity.badRequest().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Bio name is already in use", HttpStatus.BAD_REQUEST.value(), new MessageResponse("Choose unique bio name")));
        }

        Set<AppRoles> userRoles = new HashSet<>();
        signupRequest.getRoles().forEach(role -> {
            switch (role) {
                case "vendor" -> userRoles.add(AppRoles.ROLE_VENDOR);
                case "buyer" -> userRoles.add(AppRoles.ROLE_BUYER);
            }
        });
        String newUserId = generateUserId();

        AppUser newUser = new AppUser(
            newUserId,
            signupRequest.getFirstName(),
            signupRequest.getLastName(),
            signupRequest.getBioName(),
            signupRequest.getEmail(),
            userRoles,
            new Location(signupRequest.getLocation().getCounty(), signupRequest.getLocation().getConstituency()),
            passwordEncoder.encode(signupRequest.getPassword()),
            signupRequest.getPhoneNumber()
        );

        appUserService.createNewUserAccount(newUser);

        // create their destination Queue on the MOM
        destinationManagementService.createPrivateDestination(newUserId);

        // If they are a vendor, create for them an empty Groups document. They can populate later if they choose to
        if(userRoles.contains(AppRoles.ROLE_VENDOR)) {
            groupsService.createGroups(Groups.builder().groupId(generateGroupsId()).vendorId(newUserId).groupItems(new ArrayList<>()).build());
        }

        return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
            "Successfully signed up user", HttpStatus.OK.value(), new MessageResponse("Successfully signed up user")));
    }

    private String generateUserId() {
        return UUID.randomUUID().toString()
            .replace("-", "").substring(0, 15);
    }

    private String generateGroupsId() {
        return UUID.randomUUID().toString()
                .replace("-", "").substring(0, 15);
    }
}
