package npc.kassinimvp.payload.response;

import lombok.*;

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
}
