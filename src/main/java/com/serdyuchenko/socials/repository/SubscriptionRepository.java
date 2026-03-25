package com.serdyuchenko.socials.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serdyuchenko.socials.entity.SubscriptionEntity;
import com.serdyuchenko.socials.entity.SubscriptionId;

// Репозиторий подписок.
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, SubscriptionId> {
}
