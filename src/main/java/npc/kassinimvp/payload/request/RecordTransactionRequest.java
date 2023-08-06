package npc.kassinimvp.payload.request;

import lombok.*;
import npc.kassinimvp.entity.definitions.BuyerDetails;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RecordTransactionRequest {
    private String toUserId; // this is me - the vendor
    private String fromUserId; // from who did the money come from - the buyer
    private Integer transactionAmount;
    private String postId;
    private String dateOfTransaction;
    private String monthOfTransaction;
    private Integer yearOfTransaction;
    private BuyerDetails buyerDetails;
}
