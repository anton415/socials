package com.serdyuchenko.socials.controller;

import java.net.URI;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.serdyuchenko.socials.dto.UserCreateRequestDto;
import com.serdyuchenko.socials.dto.UserResponseDto;
import com.serdyuchenko.socials.dto.UserUpdateRequestDto;
import com.serdyuchenko.socials.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

/**
 * REST контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Операции с пользователями")
public class UserRestController {

	private final UserService userService;

	@PostMapping
	@Operation(summary = "Создать пользователя")
	public ResponseEntity<UserResponseDto> save(@Valid @RequestBody final UserCreateRequestDto user) {
		final UserResponseDto savedUser = userService.createUser(user);
		final URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.id())
			.toUri();
		return ResponseEntity.created(location).body(savedUser);
	}

	@GetMapping("/{userId}")
	@Operation(summary = "Получить пользователя по id")
	public ResponseEntity<UserResponseDto> findById(@PathVariable final UUID userId) {
		return userService.findById(userId)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping
	@Operation(summary = "Обновить пользователя")
	public ResponseEntity<Void> update(@Valid @RequestBody final UserUpdateRequestDto user) {
		if (!userService.updateUser(user)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{userId}")
	@Operation(summary = "Удалить пользователя по id")
	public ResponseEntity<Void> deleteById(@PathVariable final UUID userId) {
		if (!userService.deleteById(userId)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}
