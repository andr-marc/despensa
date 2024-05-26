package com.andremarc.pantry.controller;

import com.andremarc.pantry.entity.Product;
import com.andremarc.pantry.model.ApiResponse;
import com.andremarc.pantry.model.PaginatedData;
import com.andremarc.pantry.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService service;

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> save(@RequestBody Product product){
        ApiResponse<Product> response = service.save(product);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> save(@PathVariable UUID id){
        ApiResponse<Product> response = service.getById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedData<Product>>> getAll(@PageableDefault() Pageable pageable){

        ApiResponse<PaginatedData<Product>> response = service.getAll(pageable);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Product>> update(@RequestBody Product product){
        ApiResponse<Product> response = service.update(product);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable UUID id){
        ApiResponse<?> response = service.delete(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
