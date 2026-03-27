package com.serdyuchenko.socials.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

// DTO ответа поста.
public record PostResponseDto(
	UUID id,
	UUID userId,
	String title,
	String text,
	OffsetDateTime created
) {
}
