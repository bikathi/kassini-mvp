package npc.kassinimvp.entity.definitions;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BuyerDetails {
    public String userId;
    public String userFirstName;
    public String userLastName;
    public String bioName;

    public BuyerDetails(BuyerDetails impliedDetails) {
        this.userId = impliedDetails.getUserId();
        this.userFirstName = impliedDetails.getUserFirstName();
        this.userLastName = impliedDetails.getUserLastName();
        this.bioName = impliedDetails.getBioName();
    }
}
