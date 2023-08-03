package npc.kassinimvp.repository;

import npc.kassinimvp.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends MongoRepository<AppUser, String> {
    // a custom method to find user details of whoever's logging in by their email
    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
