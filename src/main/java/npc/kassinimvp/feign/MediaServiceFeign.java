package npc.kassinimvp.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "media-service")
public interface MediaServiceFeign {
    @PostMapping(value = "/storage/create-chat-storage")
    ResponseEntity<?> createNewChatStorageLocation(@RequestParam String chatId);

    @DeleteMapping(value = "/storage/del-chat-storage")
    ResponseEntity<?> deleteChatStorageLocation(@RequestParam String chatId);
}
