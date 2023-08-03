package npc.kassinimvp.security.service;

import npc.kassinimvp.entity.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Integer phoneNumber;

    // custom constructors
    public UserDetailsImpl(String firstName, String lastName, String email, String password, Integer phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public static UserDetailsImpl build(AppUser user) {
        // we can handle the role conversion from Set<?> to List<?> here
        return new UserDetailsImpl(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPhoneNumber());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return firstName + " "  +lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
