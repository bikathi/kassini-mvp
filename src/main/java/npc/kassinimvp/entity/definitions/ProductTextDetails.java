package npc.kassinimvp.entity.definitions;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductTextDetails {
    private String productImage;
    private String postId;
    private String productName;
    private String vendorName;
    private String productDescription;
}
