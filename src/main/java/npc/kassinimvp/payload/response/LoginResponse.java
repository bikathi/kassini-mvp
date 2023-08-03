package npc.kassinimvp.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String email;
    private Integer phoneNumber;
    private String authToken;
}
