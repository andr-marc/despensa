package com.andremarc.pantry.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity
@Table(name = "categoria")
public class Category {
        @Id
        @UuidGenerator
        UUID id;

        @Column(name = "nome", nullable = false)
        String name;
}