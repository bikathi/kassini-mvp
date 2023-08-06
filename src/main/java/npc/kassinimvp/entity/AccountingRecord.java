package npc.kassinimvp.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "accounting-records")
public class AccountingRecord {
    @Id
    private String accountingId;
    private String dateOfTransaction;
    private Integer transactionAmount;
    private String fromUserId;
    private String toUserId;
    private String monthOfTransaction;
    private Integer yearOfTransaction;
}
