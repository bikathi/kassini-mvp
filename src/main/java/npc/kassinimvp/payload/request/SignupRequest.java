package npc.kassinimvp.payload.request;

import lombok.*;

import java.util.Set;
import npc.kassinimvp.entity.definitions.Location;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String bioName; // e.g @anonCabbage
    private String email;
    private String password;
    private Long phoneNumber;
    private Location location;
    private Set<String> roles;
}


