package com.serdyuchenko.socials.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Сущность дружбы.
@Entity
@Table(name = "friendships")
@Getter
@Setter
@NoArgsConstructor
public class FriendshipEntity {
	@EmbeddedId
	private FriendshipId id = new FriendshipId();

	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@MapsId("friendId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "friend_id", nullable = false)
	private UserEntity friend;
}
