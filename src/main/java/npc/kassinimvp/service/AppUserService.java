package npc.kassinimvp.service;

import npc.kassinimvp.entity.AppUser;
import npc.kassinimvp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;

    public boolean userExistsByEmail(String email) {
        return appUserRepository.existsByEmail(email);
    }

    public void createNewUserAccount(AppUser newUser) {
        appUserRepository.save(newUser);
    }
}