package com.serdyuchenko.socials.repository;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.serdyuchenko.socials.entity.PostEntity;
import com.serdyuchenko.socials.entity.UserEntity;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

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

	@Test
	@DisplayName("Должен возвращать посты только указанного пользователя")
	public void whenFindAllByUserThenReturnOnlyUserPosts() {
		var firstUser = saveUser("author-one", "author-one@example.com");
		var secondUser = saveUser("author-two", "author-two@example.com");
		postRepository.save(createPost(firstUser, "First user post", "Text"));
		postRepository.save(createPost(secondUser, "Second user post", "Text"));

		var posts = postRepository.findAllByUser(firstUser);

		assertThat(posts)
			.hasSize(1)
			.extracting(PostEntity::getTitle)
			.containsExactly("First user post");
	}

	@Test
	@DisplayName("Должен возвращать посты в указанном диапазоне даты создания")
	public void whenFindAllByCreatedBetweenThenReturnPostsInRange() {
		var user = saveUser("author", "author@example.com");
		var firstPost = postRepository.save(createPost(user, "First post", "Text"));
		var secondPost = postRepository.save(createPost(user, "Second post", "Text"));
		var thirdPost = postRepository.save(createPost(user, "Third post", "Text"));
		updateCreated(firstPost, OffsetDateTime.parse("2026-03-20T10:00:00Z"));
		updateCreated(secondPost, OffsetDateTime.parse("2026-03-21T12:00:00Z"));
		updateCreated(thirdPost, OffsetDateTime.parse("2026-03-22T14:00:00Z"));

		var posts = postRepository.findAllByCreatedBetween(
			OffsetDateTime.parse("2026-03-21T11:00:00Z"),
			OffsetDateTime.parse("2026-03-22T13:00:00Z")
		);

		assertThat(posts)
			.hasSize(1)
			.extracting(PostEntity::getTitle)
			.containsExactly("Second post");
	}

	@Test
	@DisplayName("Должен возвращать последние посты по дате создания с пагинацией")
	public void whenFindAllByOrderByCreatedDescThenReturnPostsWithPaging() {
		var user = saveUser("author", "author@example.com");
		var firstPost = postRepository.save(createPost(user, "First post", "Text"));
		var secondPost = postRepository.save(createPost(user, "Second post", "Text"));
		var thirdPost = postRepository.save(createPost(user, "Third post", "Text"));
		updateCreated(firstPost, OffsetDateTime.parse("2026-03-20T10:00:00Z"));
		updateCreated(secondPost, OffsetDateTime.parse("2026-03-21T12:00:00Z"));
		updateCreated(thirdPost, OffsetDateTime.parse("2026-03-22T14:00:00Z"));

		var posts = postRepository.findAllByOrderByCreatedDesc(PageRequest.of(0, 2));

		assertThat(posts)
			.hasSize(2)
			.extracting(PostEntity::getTitle)
			.containsExactly("Third post", "Second post");
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

	// нужен только для того, чтобы в тестах сделать created предсказуемым
	private void updateCreated(final PostEntity post, final OffsetDateTime created) {
		jdbcTemplate.update("UPDATE posts SET created = ? WHERE id = ?", created, post.getId());
	}
}
