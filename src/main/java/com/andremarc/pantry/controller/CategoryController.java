package com.andremarc.pantry.controller;

import com.andremarc.pantry.entity.Category;
import com.andremarc.pantry.model.ApiResponse;
import com.andremarc.pantry.model.PaginatedData;
import com.andremarc.pantry.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> save(@RequestBody Category agency){
        ApiResponse<Category> agencieApiResponse = service.save(agency);
        return ResponseEntity.status(agencieApiResponse.getStatus()).body(agencieApiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> save(@PathVariable UUID id){
        ApiResponse<Category> agencieApiResponse = service.getById(id);
        return ResponseEntity.status(agencieApiResponse.getStatus()).body(agencieApiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedData<Category>>> getAll(@PageableDefault() Pageable pageable){

        ApiResponse<PaginatedData<Category>> agencieApiResponse = service.getAll(pageable);
        return ResponseEntity.status(agencieApiResponse.getStatus()).body(agencieApiResponse);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Category>> update(@RequestBody Category agency){
        ApiResponse<Category> agencieApiResponse = service.update(agency);
        return ResponseEntity.status(agencieApiResponse.getStatus()).body(agencieApiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable UUID id){
        ApiResponse<?> agencyApiResponse = service.delete(id);
        return ResponseEntity.status(agencyApiResponse.getStatus()).body(agencyApiResponse);
    }

}
