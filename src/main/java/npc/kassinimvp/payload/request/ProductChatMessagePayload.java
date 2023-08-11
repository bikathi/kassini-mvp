package npc.kassinimvp.payload.request;

import lombok.*;
import npc.kassinimvp.entity.definitions.ProductTextDetails;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProductChatMessagePayload extends TextChatMessagePayload {
    private ProductTextDetails productTextDetails;
}
