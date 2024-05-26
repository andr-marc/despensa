package com.andremarc.pantry.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Table(schema = "entidade", name = "permissao")
public class Permission {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "nome")
    private String name;
}
