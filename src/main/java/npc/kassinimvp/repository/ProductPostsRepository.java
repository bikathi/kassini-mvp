package npc.kassinimvp.repository;

import npc.kassinimvp.entity.ProductPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPostsRepository extends MongoRepository<ProductPost, String> {
    boolean existsByPostId(String postId);
    void deletePostByPostId(String postId);

    /**
     * A custom method to find posts based on the userID of the vendor
     */
    Page<ProductPost> findAllByVendorId(String vendorId, Pageable page);

    /**
     * A custom method to find posts generally in a page
     */
    Page<ProductPost> findAll(Pageable page);
}
