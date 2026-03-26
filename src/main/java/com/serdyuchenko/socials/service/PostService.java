package com.serdyuchenko.socials.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.serdyuchenko.socials.entity.ImageEntity;
import com.serdyuchenko.socials.entity.PostEntity;
import com.serdyuchenko.socials.repository.ImageRepository;
import com.serdyuchenko.socials.repository.PostRepository;
import com.serdyuchenko.socials.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Сервис для управления постами. Позволяет создавать, обновлять и удалять посты, а также сохранять связанные изображения.
 */
@Service
@RequiredArgsConstructor
public class PostService {

	private final UserRepository userRepository;

	private final PostRepository postRepository;

	private final ImageRepository imageRepository;

	@Transactional
	public PostEntity createPost(
		final UUID userId,
		final String title,
		final String text,
		final List<String> imageUrls
	) {
		var user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + userId));
		var post = new PostEntity();
		post.setUser(user);
		post.setTitle(title);
		post.setText(text);
		var savedPost = postRepository.save(post);
		saveImages(savedPost, imageUrls);
		return savedPost;
	}

	@Transactional
	public PostEntity createPost(final UUID userId, final String title, final String text) {
		return createPost(userId, title, text, List.of());
	}

	@Transactional
	public PostEntity updatePost(
		final UUID userId,
		final UUID postId,
		final String title,
		final String text
	) {
		var post = findOwnedPost(userId, postId);
		post.setTitle(title);
		post.setText(text);
		return postRepository.save(post);
	}

	@Transactional
	public void deletePost(final UUID userId, final UUID postId) {
		findOwnedPost(userId, postId);
		imageRepository.deleteAllByPostId(postId);
		postRepository.deletePostById(postId);
	}

	private void saveImages(final PostEntity post, final List<String> imageUrls) {
		if (imageUrls == null || imageUrls.isEmpty()) {
			return;
		}
		var images = imageUrls.stream()
			.map(imageUrl -> createImage(post, imageUrl))
			.toList();
		imageRepository.saveAll(images);
	}

	private ImageEntity createImage(final PostEntity post, final String imageUrl) {
		var image = new ImageEntity();
		image.setPost(post);
		image.setImageUrl(imageUrl);
		return image;
	}

	private PostEntity findOwnedPost(final UUID userId, final UUID postId) {
		var post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("Пост не найден: " + postId));
		if (!post.getUser().getId().equals(userId)) {
			throw new IllegalArgumentException("Пост не принадлежит пользователю");
		}
		return post;
	}
}
