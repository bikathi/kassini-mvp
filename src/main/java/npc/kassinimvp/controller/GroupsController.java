package npc.kassinimvp.controller;

import lombok.extern.slf4j.Slf4j;
import npc.kassinimvp.entity.AppUser;
import npc.kassinimvp.entity.Groups;
import npc.kassinimvp.entity.definitions.GroupItem;
import npc.kassinimvp.payload.request.CreateGroupItemRequest;
import npc.kassinimvp.payload.response.GenericServiceResponse;
import npc.kassinimvp.service.AfricasTalkingService;
import npc.kassinimvp.service.GroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/groups")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class GroupsController {
    @Autowired
    private GroupsService groupsService;

    @Autowired
    private AfricasTalkingService africasTalkingService;

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
                .phoneNumber(minimAppUser.getPhoneNumber())
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
            log.error("Error encountered while creating groupItem of vendor {}. Details: {}", vendorId, ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Failed to create item. Please Try Again.", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @PatchMapping(value = "/add-gitem-member")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> addGroupItemMember(
            @RequestParam String vendorId, @RequestParam String groupItemId, @RequestBody CreateGroupItemRequest.MinimAppUser memberMinimDetails) {
        try {
            // retrieve groups by vendorId
            Groups vendorGroups = groupsService.findGroupsByVendorId(vendorId).orElseThrow(() -> new RuntimeException("Failed to find groups for vendorId: " + vendorId));

            // find the correct groupItem based on the provided ID
            for(GroupItem groupItem : vendorGroups.getGroupItems()) {
                if(groupItem.getGroupId().equals(groupItemId)) {
                    // add the new member to the list
                    groupItem.getGroupMembers().add(AppUser.builder()
                        .userId(memberMinimDetails.getUserId())
                        .phoneNumber(memberMinimDetails.getPhoneNumber())
                        .bioName(memberMinimDetails.getBioName())
                        .firstName(memberMinimDetails.getFirstName())
                        .lastName(memberMinimDetails.getLastName())
                    .build());
                    break;
                }
            }

            // update groups doc in the DB
            groupsService.updateGroupItemMembers(vendorGroups);

            // return response to user
            log.info("Update members list for group of vendor {}", vendorId);
            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully added member to list.", HttpStatus.OK.value(), null));
        } catch(Exception ex) {
            log.error("Error encountered while adding member to groups of vendor {}. Details: {}", vendorId, ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Failed to add member. Please Try Again.", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @PatchMapping(value = "/remove-gitem-member")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> removeGroupItemMember(@RequestParam String vendorId, @RequestParam String groupItemId, @RequestParam String memberId) {
        try {
            // retrieve groups by memberId
            Groups vendorGroups = groupsService.findGroupsByVendorId(vendorId).orElseThrow(() -> new RuntimeException("Failed to find groups for vendorId: " + vendorId));

            // find the correct groupItem based on the provided ID
            for(GroupItem groupItem : vendorGroups.getGroupItems()) {
                if(groupItem.getGroupId().equals(groupItemId)) {
                    // remove the member with a matching memberId from the list
                    for(AppUser groupMember : groupItem.getGroupMembers()) {
                        if(groupMember.getUserId().equals(memberId)) {
                            groupItem.getGroupMembers().remove(groupMember);
                            break;
                        }
                        break;
                    }
                }
            }

            // update groups doc in the DB
            groupsService.updateGroupItemMembers(vendorGroups);

            // return response to user
            log.info("Update members list for group of vendor {}", vendorId);
            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                    "Successfully removed member from list.", HttpStatus.OK.value(), null));
        } catch(Exception ex) {
            log.error("Error encountered while removing member from groups of vendor {}. Details: {}", vendorId, ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Failed to remove member. Please Try Again.", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @PostMapping(value = "/sms-gitem-members")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> smsGroupItemMembers(@RequestBody List<String> targetNumbers) {
        // NB: the client should have converted the Long phone number into a String and add them to the RB List<String>
        String[] recipientNumbers = targetNumbers.toArray(new String[0]);
        log.info("Recipient numbers {}", (Object) recipientNumbers);

        try {
            africasTalkingService.sendBulkSMS(recipientNumbers);
            log.info("Successfully sent SMS to target numbers");
            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully sent SMSs to recipients.", HttpStatus.OK.value(), null));
        } catch(Exception ex) {
            log.error("Error encountered while sending SMSs. Details: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Failed to send message. Please Try Again.", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    private String generateGroupItemId() {
        return UUID.randomUUID().toString()
            .replace("-", "").substring(0, 10);
    }
}
