package com.andremarc.pantry.repository;

import com.andremarc.pantry.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository  extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findUserByUsername(String username);
}
