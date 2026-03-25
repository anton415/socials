package com.serdyuchenko.socials.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serdyuchenko.socials.entity.MessageEntity;

// Репозиторий сообщений.
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
}
