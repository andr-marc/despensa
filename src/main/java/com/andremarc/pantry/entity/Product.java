package com.andremarc.pantry.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "quantidade")
public class Product {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "titulo")
    private String title;

    @Column(name = "codigo_barra")
    private String barCode;

    @Column(name = "marca")
    private String brand;

    @Column(name = "data_validade")
    private String expirationDate;

    @Column(name = "data_compra")
    private String boughtAt;

    @OneToMany(targetEntity = Category.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "categoria_id")
    private List<Category> members;

    @OneToMany(targetEntity = Unity.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "unidade_id")
    private List<Unity> unities;
}
