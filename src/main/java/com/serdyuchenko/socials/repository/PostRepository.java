package com.serdyuchenko.socials.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.serdyuchenko.socials.entity.PostEntity;
import com.serdyuchenko.socials.entity.UserEntity;

// Репозиторий постов.
public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    // Список постов пользователя
    List<PostEntity> findAllByUser(UserEntity user);

    // Список постов в диапазоне даты
    List<PostEntity> findAllByCreatedBetween(OffsetDateTime start, OffsetDateTime end);

    // Список постов отсортированный по дате с пагинацией
    List<PostEntity> findAllByOrderByCreatedDesc(Pageable pageable);
}
