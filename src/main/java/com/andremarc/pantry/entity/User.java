package com.andremarc.pantry.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity
@Table(schema = "entidades", name = "usuario")
public class User {
    @Id
    @UuidGenerator
    private UUID id;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "permissao_id")
    private Permission permission;

    @Column (name = "nome")
    private String name;

    @Column(name = "usuario")
    private String username;


    @Column(name = "senha")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "acesso_duracao")
    private String accessDuration;

}
