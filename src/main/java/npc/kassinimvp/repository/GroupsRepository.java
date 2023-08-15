package npc.kassinimvp.repository;

import npc.kassinimvp.entity.Groups;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupsRepository extends MongoRepository<Groups, String> {
    /**
     * Custom method to find a Groups object by vendorId
     */
    Optional<Groups> findByVendorId(String vendorId);
}
