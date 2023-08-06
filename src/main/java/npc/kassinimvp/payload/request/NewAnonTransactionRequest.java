package npc.kassinimvp.payload.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecordAnonTransactionRequest {
    private String toUserId; // this is me - the vendor
    private String fromUserId; // from who did the money come from - the buyer
    private Integer transactionAmount;
    private String postId;
    private String dateOfTransaction;
    private String monthOfTransaction;
    private Integer yearOfTransaction;
}
