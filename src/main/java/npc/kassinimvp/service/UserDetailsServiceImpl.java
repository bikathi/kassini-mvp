package npc.kassinimvp.service;

import npc.kassinimvp.entity.AppUser;
import npc.kassinimvp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // in our case here we are actually loading user's by their email as and not the username
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email"  + email + "not found"));

        return UserDetailsImpl.build(user);
    }
}
