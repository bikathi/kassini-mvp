package npc.kassinimvp.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PrivateMessageDTO implements Serializable {
	private String fromUserId;
	private String toUserId;
	private String message;
	private String dateSent;
}
