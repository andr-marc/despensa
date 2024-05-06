package com.andremarc.pantry.controller;

import com.andremarc.pantry.entity.Category;
import com.andremarc.pantry.entity.Quantity;
import com.andremarc.pantry.model.ApiResponse;
import com.andremarc.pantry.model.PaginatedData;
import com.andremarc.pantry.service.QuantityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quantity")
public class QuantityController {

    private final QuantityService service;

    @PostMapping
    public ResponseEntity<ApiResponse<Quantity>> save(@RequestBody Quantity quantity){
        ApiResponse<Quantity> response = service.save(quantity);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Quantity>> save(@PathVariable UUID id){
        ApiResponse<Quantity> response = service.getById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedData<Quantity>>> getAll(@PageableDefault() Pageable pageable){

        ApiResponse<PaginatedData<Quantity>> response = service.getAll(pageable);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Quantity>> update(@RequestBody Quantity quantity){
        ApiResponse<Quantity> response = service.update(quantity);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable UUID id){
        ApiResponse<?> response = service.delete(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
