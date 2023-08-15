package npc.kassinimvp.service;

import com.africastalking.AfricasTalking;
import com.africastalking.SmsService;
import com.africastalking.sms.Recipient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
@Slf4j
public class AfricasTalkingService {
    @Value("${app.information.username}")
    private String username;

    @Value("${app.information.apikey}")
    private String apiKey;

    public void sendBulkSMS(String[] recipientNumbers) {
        // initialize the SDK
        AfricasTalking.initialize(username, apiKey);

        // get the SMS service
        SmsService smsService = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

        // the message
        String messageString = "Hello from Kassini. This is a test message.";

        // sending the sms
        try {
            List<Recipient> statusResponse = smsService.send(messageString, recipientNumbers, true);
            log.info("Sent SMSs successfully....");
            statusResponse.forEach(recipient -> log.info("Status of message to recipient of number {}: {}", recipient.number, recipient.status));
        } catch(Exception ex) {
            log.info("Error encountered while reaching out to prospects. Details: {}", ex.getMessage());
        }
    }
}
