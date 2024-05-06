package com.andremarc.pantry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity
@Table(name = "quantidade")
public class Quantity {
    @Id
    @UuidGenerator
    UUID id;

    @Column(name = "unidade", nullable = false)
    String unity;

    @Column(name = "subunidade")
    String subUnity;
}
