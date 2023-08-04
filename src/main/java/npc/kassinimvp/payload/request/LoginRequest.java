package npc.kassinimvp.payload.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
