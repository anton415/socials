package com.serdyuchenko.socials.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import com.serdyuchenko.socials.entity.UserEntity;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest extends AbstractRepositoryTest {

	// Очищает данные
	@BeforeEach
	public void setUp() {
		cleanDatabase();
	}

	@Test
	@DisplayName("Должен сохранять пользователя и находить по идентификатору")
	public void whenSaveUserThenFindById() {
		var person = createUser("John Doe", "john.doe@example.com");
		userRepository.save(person);
		var foundPerson = userRepository.findById(person.getId());
		assertThat(foundPerson).isPresent();
		assertThat(foundPerson.get().getUsername()).isEqualTo("John Doe");
	}

	@Test
	@DisplayName("Должен возвращать всех сохраненных пользователей")
	public void whenFindAllThenReturnAllUsers() {
		var person1 = createUser("John Doe", "john.doe@example.com");
		var person2 = createUser("Jane Doe", "jane.doe@example.com");
		userRepository.save(person1);
		userRepository.save(person2);
		var persons = userRepository.findAll();
		assertThat(persons).hasSize(2);
		assertThat(persons).extracting(UserEntity::getUsername).contains("John Doe", "Jane Doe");
	}

	@Test
	@DisplayName("Должен находить пользователя по логину и паролю")
	public void whenFindByUsernameAndPasswordThenReturnUser() {
		var person = createUser("john", "john@example.com");
		person.setPassword("secret");
		userRepository.save(person);

		var foundPerson = userRepository.findByUsernameAndPassword("john", "secret");

		assertThat(foundPerson).isPresent();
		assertThat(foundPerson.get().getEmail()).isEqualTo("john@example.com");
	}

	// Создает пользователя для теста
	private UserEntity createUser(final String username, final String email) {
		var user = new UserEntity();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword("password");
		return user;
	}
}
