package npc.kassinimvp.payload.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateProductPostRequest {
    private String productName;
    private Integer productCost;
    private List<String> imageLinks = new ArrayList<>();
}
