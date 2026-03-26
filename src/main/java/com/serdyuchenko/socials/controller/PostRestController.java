package com.serdyuchenko.socials.controller;

import java.net.URI;
import java.util.UUID;

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
		final PostEntity savedPost = postRepository.save(post);
		final URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedPost.getId())
			.toUri();
		return ResponseEntity.created(location).body(savedPost);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostEntity> findById(@PathVariable final UUID postId) {
		return postRepository.findById(postId)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping
	public ResponseEntity<Void> update(@RequestBody final PostEntity post) {
		if (post.getId() == null || !postRepository.existsById(post.getId())) {
			return ResponseEntity.notFound().build();
		}
		postRepository.save(post);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> deleteById(@PathVariable final UUID postId) {
		if (!postRepository.existsById(postId)) {
			return ResponseEntity.notFound().build();
		}
		postRepository.deleteById(postId);
		return ResponseEntity.noContent().build();
	}
}
