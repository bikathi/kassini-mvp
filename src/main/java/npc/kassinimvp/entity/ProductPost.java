package npc.kassinimvp.entity;

import lombok.*;
import npc.kassinimvp.entity.definitions.Product;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product-posts")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductPost {
    @Id
    private String postId;
    private String vendorId;
    private String vendorName;
    private Product product;
    private String dateOfPost;
    // this here is the number of people who showed interest in this product - this will act as our 'likes'
    private Integer numberOfEngagements;
    private Boolean sold; // by default is false
}
