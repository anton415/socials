package com.serdyuchenko.socials.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serdyuchenko.socials.entity.ImageEntity;

// Репозиторий изображений.
public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
}
