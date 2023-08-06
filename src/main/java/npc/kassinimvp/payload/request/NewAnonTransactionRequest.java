package npc.kassinimvp.payload.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewAnonTransactionRequest {
    private String toUserId; // this can be vendor or buyer
    private String fromUserId; // this can be vendor or buyer
    private Integer transactionAmount;
    private String moneyDirection;
    private String dateOfTransaction;
    private String monthOfTransaction;
    private Integer yearOfTransaction;
}
