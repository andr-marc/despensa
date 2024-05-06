package com.andremarc.pantry.service;

import com.andremarc.pantry.entity.Category;
import com.andremarc.pantry.model.ApiResponse;
import com.andremarc.pantry.model.PaginatedData;
import com.andremarc.pantry.model.Pagination;
import com.andremarc.pantry.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    public ApiResponse<Category> save(Category category) {
        ApiResponse<Category> response = new ApiResponse<>();
        Category saved = repository.save(category);
        return response.of(HttpStatus.OK, "Categoria salva com sucesso!", saved);
    }

    public ApiResponse<Category> update(Category category) {
        ApiResponse<Category> response = new ApiResponse<>();

        if(category.getId() == null)
            return response.of(HttpStatus.BAD_REQUEST, "ID informado!");

        if (!repository.existsById(category.getId()))
            return response.of(HttpStatus.NOT_FOUND, "Categoria não encontrada com o ID informado");

        Category saved = repository.save(category);
        return response.of(HttpStatus.OK, "Categoria salva com sucesso!", saved);
    }

    public ApiResponse<Category> getById(UUID id) {
        ApiResponse<Category> response = new ApiResponse<>();
        repository.findById(id).ifPresentOrElse(
                it -> response.of(HttpStatus.OK, "Categoria encontrada!", it),
                () -> response.of(HttpStatus.NOT_FOUND, "Categoria não encontrada com o ID informado")
        );
        return response;
    }
    public ApiResponse<PaginatedData<Category>> getAll(Pageable pageable) {
        ApiResponse<PaginatedData<Category>> response = new ApiResponse<>();

        pageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(pageable.getSort().stream().toList())
        );

        Page<Category> category = repository.findAll(pageable);
        final Pagination pagination = Pagination.from(category, pageable);

        return response.of(HttpStatus.OK, "Lista de categorias encontrado com sucesso!", new PaginatedData<>(category.getContent(), pagination));
    }

    public ApiResponse<?> delete(UUID id) {
        ApiResponse<?> response = new ApiResponse<>();
        repository.findById(id).ifPresentOrElse(
                it -> {
                    response.of(HttpStatus.OK, "Categoria deletada com sucesso!");
                    repository.deleteById(id);
                },
                () -> response.of(HttpStatus.NOT_FOUND, "Categoria não encontrada com o ID informado")
        );
        return response;
    }
}
