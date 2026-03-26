package com.serdyuchenko.socials.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

// DTO пользователя с его постами.
public record UserPostsDto(
	UUID userId,
	String username,
	List<PostDto> posts
) {
	// DTO  поста в ответе пользователя.
	public record PostDto(
		UUID id,
		String title,
		String text,
		OffsetDateTime created
	) {
	}
}
