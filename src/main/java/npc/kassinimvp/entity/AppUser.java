package npc.kassinimvp.entity;


import lombok.*;
import npc.kassinimvp.entity.definitions.AppRoles;
import npc.kassinimvp.entity.definitions.Location;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "app-users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppUser {
    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private String bioName;
    private String email;
    private Set<AppRoles> userRoles = new HashSet<>();
    private Location userLocation;
    private String password;
    private Long phoneNumber;
}


