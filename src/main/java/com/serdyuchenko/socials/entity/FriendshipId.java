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

// Составной ключ дружбы.
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FriendshipId implements Serializable {
	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "friend_id", nullable = false)
	private UUID friendId;
}
