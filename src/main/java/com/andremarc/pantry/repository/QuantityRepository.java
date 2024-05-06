package com.andremarc.pantry.repository;

import com.andremarc.pantry.entity.Quantity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuantityRepository  extends JpaRepository<Quantity, UUID> {
}