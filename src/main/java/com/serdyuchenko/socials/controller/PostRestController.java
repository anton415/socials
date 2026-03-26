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

import com.serdyuchenko.socials.entity.PostEntity;
import com.serdyuchenko.socials.repository.PostRepository;

import lombok.RequiredArgsConstructor;

/**
 * REST контроллер для управления постами.
 */
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostRestController {

	private final PostRepository postRepository;

	@PostMapping
	public ResponseEntity<PostEntity> save(@RequestBody final PostEntity post) {
		return ResponseEntity.ok(postRepository.save(post));
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostEntity> findById(@PathVariable final UUID postId) {
		return postRepository.findById(postId)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public void update(@RequestBody final PostEntity post) {
		postRepository.save(post);
	}

	@DeleteMapping("/{postId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable final UUID postId) {
		postRepository.deleteById(postId);
	}
}
