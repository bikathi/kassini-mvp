package npc.kassinimvp.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import npc.kassinimvp.entity.ProductPost;
import npc.kassinimvp.entity.definitions.Product;
import npc.kassinimvp.payload.request.CreateProductPostRequest;
import npc.kassinimvp.payload.request.UpdateProductPostRequest;
import npc.kassinimvp.payload.response.GenericServiceResponse;
import npc.kassinimvp.payload.response.LoginResponse;
import npc.kassinimvp.payload.response.MessageResponse;
import npc.kassinimvp.security.service.UserDetailsImpl;
import npc.kassinimvp.service.ProductPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/posts")
@Slf4j
public class ProductPostController {
    @Autowired
    private ProductPostService postService;

    @Value("${service.api.version}")
    private String apiVersion;

    @Value("${service.organization.name}")
    private String organizationName;

    @PostMapping(value = "/create-post")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> createProductPost(@RequestBody CreateProductPostRequest createPostRequest) {
        // create a LocalDateTime object to represent when the new post was made
        // the date will be in the format dd/MM/yyyy
        LocalDateTime dateTimeNow = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:MM a");

        // convert the payload request into a ProductPost object
        String postId = generatePostId();
        ProductPost newPost = new ProductPost(
            postId,
            createPostRequest.getVendorId(),
            new Product(
                createPostRequest.getNewProductDetails().getProductName(),
                createPostRequest.getNewProductDetails().getProductCost(),
                new ArrayList<>(createPostRequest.getNewProductDetails().getImageLinks())
            ),
            dateTimeNow.format(dateTimeFormatter),
            0,
            false
        );

        // save it in the database and return a response to the user
        try {
            ProductPost savedResponse = postService.createNewProductPost(newPost);
            log.info("User {} created new post of ID: {}", userDetails().getUserId(), postId);
            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully created new post", HttpStatus.OK.value(), savedResponse));
        } catch(Exception ex) {
            log.error("Failed when creating new post. Details: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Server experienced an error. Please try again", HttpStatus.INTERNAL_SERVER_ERROR.value(), new MessageResponse("Failed to create new product post")));
        }
    }

    @PutMapping(value = "/update-post")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> updateProductPost(@RequestParam String postId, @RequestBody UpdateProductPostRequest updatePostRequest) {
        // confirm that the post actually exists
        boolean postExists = postService.postExistsById(postId);
        if(!postExists) {
            return ResponseEntity.badRequest().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Server experienced an error. Please try again", HttpStatus.BAD_REQUEST.value(), new MessageResponse("Invalid post ID")));
        }

        // save the post and return a response to the user
        try {
            ProductPost updatedProductPost = postService.updateExistingPost(postId, updatePostRequest);
            log.info("User {} updated post of ID {}", userDetails().getUserId(), postId);
            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully updated the post", HttpStatus.OK.value(), updatedProductPost));
        } catch(Exception ex) {
            log.error("Failed when updating post. Details: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Server experienced an error. Please try again", HttpStatus.INTERNAL_SERVER_ERROR.value(), new MessageResponse("Failed to create new product post")));
        }
    }

    @PatchMapping(value = "/status-update")
    public ResponseEntity<?> updateProductStatus() {
        return null;
    }

    @GetMapping(value = "/product-list")
    public ResponseEntity<?> getProductPostsList() {
        return null;
    }

    @GetMapping(value = "/find-product")
    public ResponseEntity<?> findProductPost() {
        return null;
    }

    @DeleteMapping(value = "/delete-post")
    public ResponseEntity<?> deleteProductPost() {
        return null;
    }

    private String generatePostId() {
        return UUID.randomUUID().toString()
                .replace("-", "").substring(0, 15);
    }

    private UserDetailsImpl userDetails() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
