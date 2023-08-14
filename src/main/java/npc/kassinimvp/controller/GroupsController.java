package npc.kassinimvp.controller;

import lombok.extern.slf4j.Slf4j;
import npc.kassinimvp.entity.AppUser;
import npc.kassinimvp.entity.definitions.GroupItem;
import npc.kassinimvp.payload.request.CreateGroupItemRequest;
import npc.kassinimvp.payload.response.GenericServiceResponse;
import npc.kassinimvp.service.GroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/groups")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class GroupsController {
    @Autowired
    private GroupsService groupsService;

    @Value("${service.api.version}")
    private String apiVersion;

    @Value("${service.organization.name}")
    private String organizationName;

    @PostMapping(value = "/create-gitem")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> createGroupItem(@RequestParam String vendorId, @RequestBody CreateGroupItemRequest createGroupItemRequest) {
        // convert the request to a proper GroupItem
        GroupItem newGroupItem = GroupItem.builder()
            .groupId(generateGroupItemId())
            .groupName(createGroupItemRequest.getGroupName())
            .groupMembers(createGroupItemRequest.getGroupMembers().stream().map(minimAppUser -> AppUser.builder()
                .userId(minimAppUser.getUserId())
                    .bioName(minimAppUser.getBioName())
                    .firstName(minimAppUser.getFirstName())
                    .lastName(minimAppUser.getLastName())
            .build()).collect(Collectors.toList()))
        .build();

        try {
            // add the GroupItem into the list in the Groups object in the db
            groupsService.addNewGroupItem(vendorId, newGroupItem);

            // return 200 OK if everything goes well
            log.info("Vendor {} created new group item for their groups", vendorId);
            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully created item.", HttpStatus.OK.value(), null));
        } catch(Exception ex) {
            log.error("Error encountered while deleting chat. Details: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Failed to create item. Please Try Again.", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @PostMapping(value = "/add-gitem-member")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> addGroupItemMember() {
        return null;
    }

    @DeleteMapping(value = "/remove-gitem-member")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> removeGroupItemMember() {
        return null;
    }

    @PostMapping(value = "/sms-gitem-members")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> smsGroupItemMembers() {
        return null;
    }

    private String generateGroupItemId() {
        return UUID.randomUUID().toString()
            .replace("-", "").substring(0, 10);
    }
}
