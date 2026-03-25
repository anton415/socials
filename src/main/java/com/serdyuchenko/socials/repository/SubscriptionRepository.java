package com.serdyuchenko.socials.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.serdyuchenko.socials.entity.SubscriptionEntity;
import com.serdyuchenko.socials.entity.SubscriptionId;
import com.serdyuchenko.socials.entity.UserEntity;

// Репозиторий подписок.
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, SubscriptionId> {
	@Query("""
		select subscription.follower
		from SubscriptionEntity subscription
		where subscription.followee.id = :userId
		""")
	List<UserEntity> findFollowersByUserId(@Param("userId") UUID userId);
}
