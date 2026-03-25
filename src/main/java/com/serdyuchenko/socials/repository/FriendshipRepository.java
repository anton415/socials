package com.serdyuchenko.socials.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serdyuchenko.socials.entity.FriendshipEntity;
import com.serdyuchenko.socials.entity.FriendshipId;

// Репозиторий дружбы.
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, FriendshipId> {
}
