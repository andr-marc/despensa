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
@Table(name = "unidade")
public class Unity {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "unidade", nullable = false)
    private String unity;

    @Column(name = "subunidade")
    private String subUnity;

    @Column
    private String status;
}
