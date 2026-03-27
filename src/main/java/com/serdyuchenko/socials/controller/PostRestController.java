package com.serdyuchenko.socials.controller;

import java.net.URI;
import java.util.List;
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

import com.serdyuchenko.socials.dto.PostCreateRequestDto;
import com.serdyuchenko.socials.dto.PostResponseDto;
import com.serdyuchenko.socials.dto.PostUpdateRequestDto;
import com.serdyuchenko.socials.dto.UserPostsDto;
import com.serdyuchenko.socials.service.PostService;

import lombok.RequiredArgsConstructor;

/**
 * REST контроллер для управления постами.
 */
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostRestController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<PostResponseDto> save(@Valid @RequestBody final PostCreateRequestDto post) {
		final PostResponseDto savedPost = postService.createPost(post);
		final URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedPost.id())
			.toUri();
		return ResponseEntity.created(location).body(savedPost);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostResponseDto> findById(@PathVariable final UUID postId) {
		return postService.findById(postId)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/by-users")
	public ResponseEntity<List<UserPostsDto>> findByUserIds(@RequestBody final List<UUID> userIds) {
		return ResponseEntity.ok(postService.findByUserIds(userIds));
	}

	@PutMapping
	public ResponseEntity<Void> update(@Valid @RequestBody final PostUpdateRequestDto post) {
		if (!postService.updatePost(post)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> deleteById(@PathVariable final UUID postId) {
		if (!postService.deleteById(postId)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}
