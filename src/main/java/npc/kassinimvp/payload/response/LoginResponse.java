package npc.kassinimvp.payload.response;

import lombok.*;

import java.util.Set;
import npc.kassinimvp.entity.definitions.Location;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LoginResponse {
    private String userId;
    private String username;
    private String bioName;
    private String email;
    private Long phoneNumber;
    private String authToken;
    private Location location;
    private Set<String> roles;
}
