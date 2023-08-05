package npc.kassinimvp.entity;

import lombok.*;
import npc.kassinimvp.entity.definitions.Product;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "product-post")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductPost {
    private String postId;
    private String vendorId;
    private Product product;
    private LocalDate dateOfPost;
    // this here is the number of people who showed interest in this product - this will act as our 'likes'
    private Integer numberOfEngagements;
    private Boolean sold; // by default is false
}
