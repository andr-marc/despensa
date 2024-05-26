package com.andremarc.pantry.service;

import com.andremarc.pantry.entity.Unity;
import com.andremarc.pantry.model.ApiResponse;
import com.andremarc.pantry.model.PaginatedData;
import com.andremarc.pantry.model.Pagination;
import com.andremarc.pantry.repository.UnityRepository;
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
public class UnityService {
    private final UnityRepository repository;

    public ApiResponse<Unity> save(Unity unity) {
        ApiResponse<Unity> response = new ApiResponse<>();
        Unity saved = repository.save(unity);
        return response.of(HttpStatus.CREATED, "Unidade salva com sucesso!", saved);
    }

    public ApiResponse<Unity> update(Unity unity) {
        ApiResponse<Unity> response = new ApiResponse<>();

        if(unity.getId() == null)
            return response.of(HttpStatus.BAD_REQUEST, "ID não informado!");

        if (!repository.existsById(unity.getId()))
            return response.of(HttpStatus.NOT_FOUND, "Unidade não encontrada com o ID informado");

        Unity saved = repository.save(unity);
        return response.of(HttpStatus.OK, "Unidade salva com sucesso!", saved);
    }

    public ApiResponse<Unity> getById(UUID id) {
        ApiResponse<Unity> response = new ApiResponse<>();
        repository.findById(id).ifPresentOrElse(
                it -> response.of(HttpStatus.OK, "Unidade encontrada!", it),
                () -> response.of(HttpStatus.NOT_FOUND, "Unidade não encontrada com o ID informado")
        );
        return response;
    }
    public ApiResponse<PaginatedData<Unity>> getAll(Pageable pageable) {
        ApiResponse<PaginatedData<Unity>> response = new ApiResponse<>();

        pageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(pageable.getSort().stream().toList())
        );

        Page<Unity> quantity = repository.findAll(pageable);
        final Pagination pagination = Pagination.from(quantity, pageable);

        return response.of(HttpStatus.OK, "Lista de unidades encontrada com sucesso!", new PaginatedData<>(quantity.getContent(), pagination));
    }

    public ApiResponse<?> delete(UUID id) {
        ApiResponse<?> response = new ApiResponse<>();
        repository.findById(id).ifPresentOrElse(
                it -> {
                    response.of(HttpStatus.OK, "Unidade deletada com sucesso!");
                    repository.deleteById(id);
                },
                () -> response.of(HttpStatus.NOT_FOUND, "Unidade não encontrada com o ID informado")
        );
        return response;
    }
}
