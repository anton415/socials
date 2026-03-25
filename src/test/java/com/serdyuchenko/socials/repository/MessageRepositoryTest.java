package com.serdyuchenko.socials.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.serdyuchenko.socials.entity.MessageEntity;
import com.serdyuchenko.socials.entity.UserEntity;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MessageRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	private MessageRepository messageRepository;

	@BeforeEach
	public void setUp() {
		cleanDatabase();
	}

	@Test
	@DisplayName("Должен сохранять сообщение и находить по идентификатору")
	public void whenSaveMessageThenFindById() {
		var sender = saveUser("sender", "sender@example.com");
		var recipient = saveUser("recipient", "recipient@example.com");
		var message = new MessageEntity();
		message.setSender(sender);
		message.setRecipient(recipient);
		message.setText("Hello");
		var savedMessage = messageRepository.save(message);

		var foundMessage = messageRepository.findById(savedMessage.getId());

		assertThat(foundMessage).isPresent();
		assertThat(foundMessage.get().getText()).isEqualTo("Hello");
	}

	@Test
	@DisplayName("Должен возвращать все сохраненные сообщения")
	public void whenFindAllThenReturnAllMessages() {
		var sender = saveUser("sender", "sender@example.com");
		var recipient = saveUser("recipient", "recipient@example.com");
		messageRepository.save(createMessage(sender, recipient, "Hello"));
		messageRepository.save(createMessage(sender, recipient, "How are you?"));

		var messages = messageRepository.findAll();

		assertThat(messages).hasSize(2);
		assertThat(messages)
			.extracting(MessageEntity::getText)
			.containsExactlyInAnyOrder("Hello", "How are you?");
	}

	private MessageEntity createMessage(
		final UserEntity sender,
		final UserEntity recipient,
		final String text
	) {
		var message = new MessageEntity();
		message.setSender(sender);
		message.setRecipient(recipient);
		message.setText(text);
		return message;
	}

	private UserEntity saveUser(final String username, final String email) {
		var user = new UserEntity();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword("password");
		return userRepository.save(user);
	}
}
