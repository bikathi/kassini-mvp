package npc.kassinimvp.service;

import npc.kassinimvp.entity.ProductPost;
import npc.kassinimvp.payload.request.UpdateProductPostRequest;
import npc.kassinimvp.repository.ProductPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ProductPostService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ProductPostsRepository productPostsRepository;

    public ProductPost createNewProductPost(ProductPost newProductPost) {
        return productPostsRepository.save(newProductPost);
    }

    public boolean postExistsById(String postId) {
        return productPostsRepository.existsByPostId(postId);
    }

    public ProductPost updateExistingPost(String postId, UpdateProductPostRequest updatePostRequest) {
        Query query = new Query(Criteria.where("postId").is(postId));
        Update updateDefinition = new Update().set("product", updatePostRequest);

        FindAndModifyOptions updateOptions = new FindAndModifyOptions().returnNew(true).upsert(false);
        return mongoTemplate.findAndModify(query, updateDefinition, updateOptions, ProductPost.class);
    }

    public void updateProductStatus(String postId) {
        Query query = new Query(Criteria.where("postId").is(postId));
        Update updateDefinition = new Update().set("sold", true);

        FindAndModifyOptions updateOptions = new FindAndModifyOptions().returnNew(false).upsert(false);
        mongoTemplate.findAndModify(query, updateDefinition, updateOptions, ProductPost.class);
    }

    public void deletePostById(String postId) {
        productPostsRepository.deletePostByPostId(postId);
    }
}
