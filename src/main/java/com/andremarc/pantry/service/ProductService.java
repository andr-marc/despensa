package com.andremarc.pantry.service;

import com.andremarc.pantry.entity.Product;
import com.andremarc.pantry.model.ApiResponse;
import com.andremarc.pantry.model.PaginatedData;
import com.andremarc.pantry.model.Pagination;
import com.andremarc.pantry.repository.ProductRepository;
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
public class ProductService {
    private final ProductRepository repository;

    public ApiResponse<Product> save(Product product) {
        ApiResponse<Product> response = new ApiResponse<>();
        Product saved = repository.save(product);
        return response.of(HttpStatus.CREATED, "Produto salvo com sucesso!", saved);
    }

    public ApiResponse<Product> update(Product product) {
        ApiResponse<Product> response = new ApiResponse<>();

        if(product.getId() == null)
            return response.of(HttpStatus.BAD_REQUEST, "ID não informado!");

        if (!repository.existsById(product.getId()))
            return response.of(HttpStatus.NOT_FOUND, "Produto não encontrado com o ID informado");

        Product saved = repository.save(product);
        return response.of(HttpStatus.OK, "Produto salvo com sucesso!", saved);
    }

    public ApiResponse<Product> getById(UUID id) {
        ApiResponse<Product> response = new ApiResponse<>();
        repository.findById(id).ifPresentOrElse(
                it -> response.of(HttpStatus.OK, "Produto encontrado!", it),
                () -> response.of(HttpStatus.NOT_FOUND, "Produto não encontrado com o ID informado")
        );
        return response;
    }

    public ApiResponse<PaginatedData<Product>> getAll(Pageable pageable) {
        ApiResponse<PaginatedData<Product>> response = new ApiResponse<>();

        pageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(pageable.getSort().stream().toList())
        );

        Page<Product> product = repository.findAll(pageable);
        final Pagination pagination = Pagination.from(product, pageable);

        return response.of(HttpStatus.OK, "Lista de produtos encontrada com sucesso!", new PaginatedData<>(product.getContent(), pagination));
    }

    public ApiResponse<?> delete(UUID id) {
        ApiResponse<?> response = new ApiResponse<>();
        repository.findById(id).ifPresentOrElse(
                it -> {
                    response.of(HttpStatus.OK, "Produto deletado com sucesso!");
                    repository.deleteById(id);
                },
                () -> response.of(HttpStatus.NOT_FOUND, "Produto não encontrado com o ID informado")
        );
        return response;
    }
}
