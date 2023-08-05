package npc.kassinimvp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/posts")
@Slf4j
public class ProductPostController {
    @PostMapping(value = "/create-post")
    public ResponseEntity<?> createProductPost() {
        return null;
    }

    @PutMapping(value = "/update-post")
    public ResponseEntity<?> updateProductPost() {
        return null;
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
}
