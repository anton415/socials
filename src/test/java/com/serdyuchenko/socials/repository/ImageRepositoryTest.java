package com.serdyuchenko.socials.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.serdyuchenko.socials.entity.ImageEntity;
import com.serdyuchenko.socials.entity.PostEntity;
import com.serdyuchenko.socials.entity.UserEntity;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImageRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private PostRepository postRepository;

	@BeforeEach
	public void setUp() {
		cleanDatabase();
	}

	@Test
	@DisplayName("Должен сохранять изображение и находить по идентификатору")
	public void whenSaveImageThenFindById() {
		var user = saveUser("author", "author@example.com");
		var post = savePost(user, "Post title", "Post text");
		var image = new ImageEntity();
		image.setPost(post);
		image.setImageUrl("https://job4j.ru/assets/img/favorite.png");
		var savedImage = imageRepository.save(image);

		var foundImage = imageRepository.findById(savedImage.getId());

		assertThat(foundImage).isPresent();
		assertThat(foundImage.get().getImageUrl()).isEqualTo("https://job4j.ru/assets/img/favorite.png");
	}

	@Test
	@DisplayName("Должен возвращать все сохраненные изображения")
	public void whenFindAllThenReturnAllImages() {
		var user = saveUser("author", "author@example.com");
		var post = savePost(user, "Post title", "Post text");
		imageRepository.save(createImage(post, "https://job4j.ru/assets/img/favorite.png"));
		imageRepository.save(createImage(post, "https://job4j.ru/img/favorite.png"));

		var images = imageRepository.findAll();

		assertThat(images).hasSize(2);
		assertThat(images)
			.extracting(ImageEntity::getImageUrl)
			.containsExactlyInAnyOrder(
				"https://job4j.ru/assets/img/favorite.png",
				"https://job4j.ru/img/favorite.png"
			);
	}

	private ImageEntity createImage(final PostEntity post, final String imageUrl) {
		var image = new ImageEntity();
		image.setPost(post);
		image.setImageUrl(imageUrl);
		return image;
	}

	private UserEntity saveUser(final String username, final String email) {
		var user = new UserEntity();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword("password");
		return userRepository.save(user);
	}

	private PostEntity savePost(final UserEntity user, final String title, final String text) {
		var post = new PostEntity();
		post.setUser(user);
		post.setTitle(title);
		post.setText(text);
		return postRepository.save(post);
	}
}
