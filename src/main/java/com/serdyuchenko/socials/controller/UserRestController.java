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

import com.serdyuchenko.socials.entity.UserEntity;
import com.serdyuchenko.socials.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * REST контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {

	private final UserRepository userRepository;

	@PostMapping
	public ResponseEntity<UserEntity> save(@Valid @RequestBody final UserEntity user) {
		final UserEntity savedUser = userRepository.save(user);
		final URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.getId())
			.toUri();
		return ResponseEntity.created(location).body(savedUser);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserEntity> findById(@PathVariable final UUID userId) {
		return userRepository.findById(userId)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping
	public ResponseEntity<Void> update(@Valid @RequestBody final UserEntity user) {
		if (user.getId() == null || !userRepository.existsById(user.getId())) {
			return ResponseEntity.notFound().build();
		}
		userRepository.save(user);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteById(@PathVariable final UUID userId) {
		if (!userRepository.existsById(userId)) {
			return ResponseEntity.notFound().build();
		}
		userRepository.deleteById(userId);
		return ResponseEntity.noContent().build();
	}
}
