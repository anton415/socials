package com.serdyuchenko.socials.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Составной ключ подписки.
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SubscriptionId implements Serializable {
	@Column(name = "follower_id", nullable = false)
	private UUID followerId;

	@Column(name = "followee_id", nullable = false)
	private UUID followeeId;
}
