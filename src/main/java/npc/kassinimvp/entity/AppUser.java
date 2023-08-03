package npc.kassinimvp.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "app-users")
public class AppUser {
    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private String email;

    private String password;
    private String phoneNumber;
}
