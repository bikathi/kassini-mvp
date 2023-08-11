package npc.kassinimvp.payload.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProductChatMessagePayload extends TextChatMessagePayload {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ProductDetails {
        private String productImage;
        private String postId;
        private String productName;
        private String vendorName;
        private String productDescription;
    }
}
