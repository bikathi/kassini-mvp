package npc.kassinimvp.payload.response;

import lombok.*;

import java.util.Set;
import npc.kassinimvp.entity.Location;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LoginResponse {
    private String username;
    private String email;
    private Long phoneNumber;
    private String authToken;
    private Location location;
    private Set<String> roles;
}
