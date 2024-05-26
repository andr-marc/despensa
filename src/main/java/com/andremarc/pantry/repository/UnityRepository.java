package com.andremarc.pantry.repository;

import com.andremarc.pantry.entity.Unity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UnityRepository  extends JpaRepository<Unity, UUID>, JpaSpecificationExecutor<Unity> {
}