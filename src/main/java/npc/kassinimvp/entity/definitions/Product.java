package npc.kassinimvp.entity.definitions;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    private String productName;
    private Integer productCost;
    private List<String> imageLinks;
}
