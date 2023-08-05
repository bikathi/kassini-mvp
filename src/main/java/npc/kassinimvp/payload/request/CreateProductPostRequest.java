package npc.kassinimvp.payload.request;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateProductPostRequest {
    private String vendorId; // same as userId
    private NewProductDetails newProductDetails;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class NewProductDetails {
        private String productName;
        private Integer productCost;
        private Set<String> imageLinks;
    }
}
