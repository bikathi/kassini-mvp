package npc.kassinimvp.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import npc.kassinimvp.payload.response.GenericServiceResponse;
import npc.kassinimvp.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    @Value("${service.api.version}")
    private String apiVersion;

    @Value("${service.organization.name}")
    private String organizationName;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("Exception type: {}", authException.getClass());
        log.error("Unauthorized error: {}", authException.getMessage());

        final ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if(authException.getClass() == InsufficientAuthenticationException.class) {
            GenericServiceResponse<MessageResponse> permissionsResponse = new GenericServiceResponse<>(apiVersion, organizationName,
                "Forbidden", HttpStatus.FORBIDDEN.value(), new MessageResponse("Insufficient permissions or invalid token for this request"));

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            mapper.writeValue(response.getOutputStream(), permissionsResponse);
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        GenericServiceResponse<MessageResponse> unauthorizedResponse = new GenericServiceResponse<>(apiVersion, organizationName,
        "Unauthorized", HttpStatus.UNAUTHORIZED.value(), new MessageResponse("Invalid authorization credentials"));

        mapper.writeValue(response.getOutputStream(), unauthorizedResponse);
    }
}
