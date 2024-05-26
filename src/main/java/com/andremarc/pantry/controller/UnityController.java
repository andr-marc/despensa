package com.andremarc.pantry.controller;

import com.andremarc.pantry.entity.Unity;
import com.andremarc.pantry.model.ApiResponse;
import com.andremarc.pantry.model.PaginatedData;
import com.andremarc.pantry.service.UnityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/unity")
public class UnityController {

    private final UnityService service;

    @PostMapping
    public ResponseEntity<ApiResponse<Unity>> save(@RequestBody Unity unity){
        ApiResponse<Unity> response = service.save(unity);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Unity>> save(@PathVariable UUID id){
        ApiResponse<Unity> response = service.getById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedData<Unity>>> getAll(@PageableDefault() Pageable pageable){
        ApiResponse<PaginatedData<Unity>> response = service.getAll(pageable);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Unity>> update(@RequestBody Unity unity){
        ApiResponse<Unity> response = service.update(unity);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable UUID id){
        ApiResponse<?> response = service.delete(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
