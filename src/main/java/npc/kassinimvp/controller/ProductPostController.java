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
import java.util.List;
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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:MMa");

        // convert the payload request into a ProductPost object
        String postId = generatePostId();
        ProductPost newPost = new ProductPost(
            postId,
            createPostRequest.getVendorId(),
            createPostRequest.getVendorName(),
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
                "Post does not exist", HttpStatus.BAD_REQUEST.value(), new MessageResponse("Invalid post ID")));
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
                "Server experienced an error. Please try again", HttpStatus.INTERNAL_SERVER_ERROR.value(), new MessageResponse("Failed to create update post")));
        }
    }

    @PatchMapping(value = "/status-update")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> updateProductStatus(@RequestParam String postId) {
        // confirm that the post actually exists
        boolean postExists = postService.postExistsById(postId);
        if(!postExists) {
            return ResponseEntity.badRequest().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Post does not exist", HttpStatus.BAD_REQUEST.value(), new MessageResponse("Invalid post ID")));
        }

        // change the post status
        try {
            postService.updateProductStatus(postId);
            log.info("User {} updated status of post {} to sold", userDetails().getUserId(), postId);
            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully updated posts status", HttpStatus.OK.value(), new MessageResponse("Post status updated successfully")));
        } catch(Exception ex) {
            log.error("Failed when updating post status. Details: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Server experienced an error. Please try again", HttpStatus.INTERNAL_SERVER_ERROR.value(), new MessageResponse("Failed to create new product post")));
        }
    }

    @GetMapping(value = "/get-posts")
    @PreAuthorize("hasAuthority('ROLE_VENDOR') or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<?> getProductPostsList(
        @RequestParam(defaultValue = "0") String page,
        @RequestParam(defaultValue = "50", required = false) String size,
        @RequestParam(required = false) String vendorId) {
        // this endpoint will cleverly handle both getting list of all available posts and the list of the
        // posts belonging to a certain userId
        Integer pageNumber = Integer.valueOf(page);
        Integer pageSize = Integer.valueOf(size);

        // this intervention will help to ensure we don't waste time on vendors that have no posts the first time a request for their posts comes in
        if(pageNumber.equals(0) && vendorId != null) {
            boolean vendorHasPosts = postService.checkIfVendorHasPosts(vendorId);
            if(!vendorHasPosts) {
                log.info("Returning empty query to user {} for vendor {} who has no posts", userDetails().getUserId(), vendorId);
                return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName, "Vendor has made no posts before.", HttpStatus.OK.value(), null));
            }
        }

        // if the vendorId is provided, we will return posts for that vendor
        if(vendorId != null) {
            try {
                List<ProductPost> vendorPosts = postService.findPostsByVendorId(vendorId, pageSize, pageNumber).toList();
                if(vendorPosts.isEmpty()) {
                    log.info("Returning no other posts by vendor {} for request of user {}", vendorId, userDetails().getUserId());
                    return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName, "No more posts to load.", HttpStatus.OK.value(), null));
                }
                return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName, "Found posts to load.", HttpStatus.OK.value(), vendorPosts));

            } catch(Exception ex) {
                log.error("Failed when getting vendor {} posts. Details: {}", userDetails().getUserId(), ex.getMessage());
                return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                    "Server experienced an error. Please try again", HttpStatus.INTERNAL_SERVER_ERROR.value(), new MessageResponse("Failed to get user posts")));
            }
        }

        // else if the userId is not provided, we simply return the available posts list
        try {
            List<ProductPost> generalPosts = postService.findPagedPostsList(pageSize, pageNumber).toList();
            if(generalPosts.isEmpty()) {
                log.info("Returning empty result posts to request of user {}", userDetails().getUserId());
                return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName, "No more posts to load.", HttpStatus.OK.value(), null));
            }

            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName, "Found posts to load.", HttpStatus.OK.value(), generalPosts));
        } catch(Exception ex) {
            log.error("Failed when getting posts. Details: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Server experienced an error. Please try again", HttpStatus.INTERNAL_SERVER_ERROR.value(), new MessageResponse("Failed to get posts")));
        }
    }

    @DeleteMapping(value = "/delete-post")
    public ResponseEntity<?> deleteProductPost(@RequestParam String postId) {
        // confirm that the post actually exists
        boolean postExists = postService.postExistsById(postId);
        if(!postExists) {
            return ResponseEntity.badRequest().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Post does not exist", HttpStatus.BAD_REQUEST.value(), new MessageResponse("Invalid post ID")));
        }

        // simply delete the post and return the response
        try {
            postService.deletePostById(postId);
            log.info("User {} deleted post of ID {}", userDetails().getUserId(), postId);
            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                    "Successfully deleted post", HttpStatus.OK.value(), new MessageResponse("Post deleted successfully")));
        } catch(Exception ex) {
            log.error("Failed when deleting post. Details: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Server experienced an error. Please try again", HttpStatus.INTERNAL_SERVER_ERROR.value(), new MessageResponse("Failed to delete post")));
        }
    }

    private String generatePostId() {
        return UUID.randomUUID().toString()
                .replace("-", "").substring(0, 15);
    }

    private UserDetailsImpl userDetails() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
