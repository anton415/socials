package com.serdyuchenko.socials.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.serdyuchenko.socials.entity.FriendshipEntity;
import com.serdyuchenko.socials.entity.FriendshipId;
import com.serdyuchenko.socials.entity.UserEntity;

// Репозиторий дружбы.
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, FriendshipId> {
	@Query("""
		select friendship.friend
		from FriendshipEntity friendship
		where friendship.user.id = :userId
		""")
	List<UserEntity> findFriendsByUserId(@Param("userId") UUID userId);
}
