package com.andremarc.pantry.service;

import com.andremarc.pantry.entity.Quantity;
import com.andremarc.pantry.model.ApiResponse;
import com.andremarc.pantry.model.PaginatedData;
import com.andremarc.pantry.model.Pagination;
import com.andremarc.pantry.repository.QuantityRepository;
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
public class QuantityService {
    private final QuantityRepository repository;

    public ApiResponse<Quantity> save(Quantity quantity) {
        ApiResponse<Quantity> response = new ApiResponse<>();
        Quantity saved = repository.save(quantity);
        return response.of(HttpStatus.OK, "Quantidade salva com sucesso!", saved);
    }

    public ApiResponse<Quantity> update(Quantity quantity) {
        ApiResponse<Quantity> response = new ApiResponse<>();

        if(quantity.getId() == null)
            return response.of(HttpStatus.BAD_REQUEST, "ID informado!");

        if (!repository.existsById(quantity.getId()))
            return response.of(HttpStatus.NOT_FOUND, "Quantidade não encontrada com o ID informado");

        Quantity saved = repository.save(quantity);
        return response.of(HttpStatus.OK, "Quantidade salva com sucesso!", saved);
    }

    public ApiResponse<Quantity> getById(UUID id) {
        ApiResponse<Quantity> response = new ApiResponse<>();
        repository.findById(id).ifPresentOrElse(
                it -> response.of(HttpStatus.OK, "Quantidade encontrada!", it),
                () -> response.of(HttpStatus.NOT_FOUND, "Quantidade não encontrada com o ID informado")
        );
        return response;
    }
    public ApiResponse<PaginatedData<Quantity>> getAll(Pageable pageable) {
        ApiResponse<PaginatedData<Quantity>> response = new ApiResponse<>();

        pageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(pageable.getSort().stream().toList())
        );

        Page<Quantity> quantity = repository.findAll(pageable);
        final Pagination pagination = Pagination.from(quantity, pageable);

        return response.of(HttpStatus.OK, "Lista de quantidade encontrado com sucesso!", new PaginatedData<>(quantity.getContent(), pagination));
    }

    public ApiResponse<?> delete(UUID id) {
        ApiResponse<?> response = new ApiResponse<>();
        repository.findById(id).ifPresentOrElse(
                it -> {
                    response.of(HttpStatus.OK, "Quantidade deletada com sucesso!");
                    repository.deleteById(id);
                },
                () -> response.of(HttpStatus.NOT_FOUND, "Quantidade não encontrada com o ID informado")
        );
        return response;
    }
}
