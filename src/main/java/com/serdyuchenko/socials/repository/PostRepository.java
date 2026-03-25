package com.serdyuchenko.socials.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serdyuchenko.socials.entity.PostEntity;

// Репозиторий постов.
public interface PostRepository extends JpaRepository<PostEntity, UUID> {
}
