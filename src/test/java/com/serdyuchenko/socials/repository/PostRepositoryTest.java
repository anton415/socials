package com.serdyuchenko.socials.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.serdyuchenko.socials.entity.PostEntity;
import com.serdyuchenko.socials.entity.UserEntity;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	private PostRepository postRepository;

	@BeforeEach
	public void setUp() {
		cleanDatabase();
	}

	@Test
	@DisplayName("Должен сохранять пост и находить по идентификатору")
	public void whenSavePostThenFindById() {
		var user = saveUser("author", "author@example.com");
		var post = new PostEntity();
		post.setUser(user);
		post.setTitle("First post");
		post.setText("Post text");
		var savedPost = postRepository.save(post);

		var foundPost = postRepository.findById(savedPost.getId());

		assertThat(foundPost).isPresent();
		assertThat(foundPost.get().getTitle()).isEqualTo("First post");
	}

	@Test
	@DisplayName("Должен возвращать все сохраненные посты")
	public void whenFindAllThenReturnAllPosts() {
		var user = saveUser("author", "author@example.com");
		postRepository.save(createPost(user, "First post", "First text"));
		postRepository.save(createPost(user, "Second post", "Second text"));

		var posts = postRepository.findAll();

		assertThat(posts).hasSize(2);
		assertThat(posts)
			.extracting(PostEntity::getTitle)
			.containsExactlyInAnyOrder("First post", "Second post");
	}

	private PostEntity createPost(final UserEntity user, final String title, final String text) {
		var post = new PostEntity();
		post.setUser(user);
		post.setTitle(title);
		post.setText(text);
		return post;
	}

	private UserEntity saveUser(final String username, final String email) {
		var user = new UserEntity();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword("password");
		return userRepository.save(user);
	}
}
