package npc.kassinimvp.security.service;

import lombok.ToString;
import npc.kassinimvp.entity.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import npc.kassinimvp.entity.Location;

@ToString
public class UserDetailsImpl implements UserDetails {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Long phoneNumber;
    private Collection<? extends GrantedAuthority> authorities;
    private Location location;

    // custom constructors
    public UserDetailsImpl(
        String firstName, String lastName, String email, String password, Long phoneNumber, Collection<? extends GrantedAuthority> authorities, Location location,
        String userId
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
        this.location = location;
        this.userId = userId;
    }

    public static UserDetailsImpl build(AppUser user) {
        // we can handle the role conversion from Set<?> to List<?> here
        List<GrantedAuthority> authorities = user.getUserRoles().stream().map(
            role -> new SimpleGrantedAuthority(role.name())
        ).collect(Collectors.toList());
        return new UserDetailsImpl(
            user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), authorities, user.getUserLocation(), user.getUserId()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Location getLocation() {
        return location;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(userId, user.userId);
    }
}
