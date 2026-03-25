package com.serdyuchenko.socials.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serdyuchenko.socials.entity.UserEntity;

// Репозиторий пользователей.
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
