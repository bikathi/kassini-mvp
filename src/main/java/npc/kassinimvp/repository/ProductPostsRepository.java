package npc.kassinimvp.repository;

import npc.kassinimvp.entity.ProductPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPostsRepository extends MongoRepository<ProductPost, String> {
}
