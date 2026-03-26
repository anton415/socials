package com.serdyuchenko.socials.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<UserEntity> save(@RequestBody final UserEntity user) {
		return ResponseEntity.ok(userRepository.save(user));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserEntity> findById(@PathVariable final UUID userId) {
		return userRepository.findById(userId)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public void update(@RequestBody final UserEntity user) {
		userRepository.save(user);
	}

	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable final UUID userId) {
		userRepository.deleteById(userId);
	}
}
