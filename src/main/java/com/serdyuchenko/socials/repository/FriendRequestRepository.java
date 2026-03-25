package com.serdyuchenko.socials.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serdyuchenko.socials.entity.FriendRequestEntity;

// Репозиторий заявок в друзья.
public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, UUID> {
}
