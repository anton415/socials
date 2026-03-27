package com.serdyuchenko.socials.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

// DTO пользователя с его постами.
public record UserPostsDto(
	UUID userId,
	@NotBlank(message = "username не может быть пустым")
	@Length(min = 6,
			max = 10,
			message = "username должно быть не менее 6 и не более 10 символов")
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
