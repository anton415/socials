package com.serdyuchenko.socials.service;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.serdyuchenko.socials.entity.UserEntity;
import com.serdyuchenko.socials.repository.AbstractRepositoryTest;
import com.serdyuchenko.socials.repository.PostRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostServiceTest extends AbstractRepositoryTest {

	@Autowired
	private PostService postService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	public void setUp() {
		cleanDatabase();
	}

	@Test
	@DisplayName("Должен создавать пост с изображениями")
	public void whenCreatePostThenSavePostWithImages() {
		var author = saveUser("author", "author@example.com");

		var savedPost = postService.createPost(
			author.getId(),
			"Post title",
			"Post text",
			List.of("img1.png", "img2.png")
		);

		var foundPost = postRepository.findById(savedPost.getId());
		var imagesCount = countImagesByPostId(savedPost.getId());

		assertThat(foundPost).isPresent();
		assertThat(foundPost.get().getTitle()).isEqualTo("Post title");
		assertThat(foundPost.get().getText()).isEqualTo("Post text");
		assertThat(foundPost.get().getUser().getId()).isEqualTo(author.getId());
		assertThat(imagesCount).isEqualTo(2);
	}

	@Test
	@DisplayName("Должен обновлять свой пост")
	public void whenUpdateOwnPostThenPostShouldBeUpdated() {
		var author = saveUser("author", "author@example.com");
		var post = postService.createPost(author.getId(), "Old title", "Old text");

		postService.updatePost(author.getId(), post.getId(), "New title", "New text");

		var updatedPost = postRepository.findById(post.getId());
		assertThat(updatedPost).isPresent();
		assertThat(updatedPost.get().getTitle()).isEqualTo("New title");
		assertThat(updatedPost.get().getText()).isEqualTo("New text");
	}

	@Test
	@DisplayName("Не должен обновлять чужой пост")
	public void whenUpdateNotOwnPostThenThrowError() {
		var author = saveUser("author", "author@example.com");
		var outsider = saveUser("outsider", "outsider@example.com");
		var post = postService.createPost(author.getId(), "Old title", "Old text");

		assertThatThrownBy(() -> postService.updatePost(
			outsider.getId(),
			post.getId(),
			"New title",
			"New text"
		)).isInstanceOf(IllegalArgumentException.class);

		var unchangedPost = postRepository.findById(post.getId());
		assertThat(unchangedPost).isPresent();
		assertThat(unchangedPost.get().getTitle()).isEqualTo("Old title");
		assertThat(unchangedPost.get().getText()).isEqualTo("Old text");
	}

	@Test
	@DisplayName("Должен удалять свой пост вместе с изображениями")
	public void whenDeleteOwnPostThenRemovePostAndImages() {
		var author = saveUser("author", "author@example.com");
		var post = postService.createPost(
			author.getId(),
			"Post title",
			"Post text",
			List.of("img1.png")
		);

		postService.deletePost(author.getId(), post.getId());

		assertThat(postRepository.findById(post.getId())).isEmpty();
		assertThat(countImagesByPostId(post.getId())).isEqualTo(0);
	}

	@Test
	@DisplayName("Не должен удалять чужой пост")
	public void whenDeleteNotOwnPostThenThrowError() {
		var author = saveUser("author", "author@example.com");
		var outsider = saveUser("outsider", "outsider@example.com");
		var post = postService.createPost(author.getId(), "Post title", "Post text");

		assertThatThrownBy(() -> postService.deletePost(outsider.getId(), post.getId()))
			.isInstanceOf(IllegalArgumentException.class);

		assertThat(postRepository.findById(post.getId())).isPresent();
	}

	/**
	 * Сохраняет пользователя в базе данных и возвращает сохранённую сущность.
	 */
	private UserEntity saveUser(final String username, final String email) {
		var user = new UserEntity();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword("password");
		return userRepository.save(user);
	}

	/**
	 * Подсчитывает количество изображений, связанных с постом.
	 * Для подтверждения удаления/создания изображений.
	 */
	private Integer countImagesByPostId(final UUID postId) {
		return jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM images WHERE post_id = ?",
			Integer.class,
			postId
		);
	}
}
