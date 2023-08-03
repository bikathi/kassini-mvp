package npc.kassinimvp.repository;

import npc.kassinimvp.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppUserRepository extends MongoRepository<AppUser, String> {
}
