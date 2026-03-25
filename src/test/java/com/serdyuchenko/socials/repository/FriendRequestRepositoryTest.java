package com.serdyuchenko.socials.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.serdyuchenko.socials.entity.FriendRequestEntity;
import com.serdyuchenko.socials.entity.UserEntity;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FriendRequestRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	private FriendRequestRepository friendRequestRepository;

	@BeforeEach
	public void setUp() {
		cleanDatabase();
	}

	@Test
	@DisplayName("Должен сохранять заявку в друзья и находить по идентификатору")
	public void whenSaveFriendRequestThenFindById() {
		var sender = saveUser("sender", "sender@example.com");
		var recipient = saveUser("recipient", "recipient@example.com");
		var friendRequest = new FriendRequestEntity();
		friendRequest.setSender(sender);
		friendRequest.setRecipient(recipient);
		friendRequest.setStatus("PENDING");
		var savedFriendRequest = friendRequestRepository.save(friendRequest);

		var foundFriendRequest = friendRequestRepository.findById(savedFriendRequest.getId());

		assertThat(foundFriendRequest).isPresent();
		assertThat(foundFriendRequest.get().getStatus()).isEqualTo("PENDING");
	}

	@Test
	@DisplayName("Должен возвращать все сохраненные заявки в друзья")
	public void whenFindAllThenReturnAllFriendRequests() {
		var sender = saveUser("sender", "sender@example.com");
		var recipient = saveUser("recipient", "recipient@example.com");
		var secondRecipient = saveUser("second-recipient", "second.recipient@example.com");
		friendRequestRepository.save(createFriendRequest(sender, recipient, "PENDING"));
		friendRequestRepository.save(createFriendRequest(sender, secondRecipient, "ACCEPTED"));

		var friendRequests = friendRequestRepository.findAll();

		assertThat(friendRequests).hasSize(2);
		assertThat(friendRequests)
			.extracting(FriendRequestEntity::getStatus)
			.containsExactlyInAnyOrder("PENDING", "ACCEPTED");
	}

	private FriendRequestEntity createFriendRequest(
		final UserEntity sender,
		final UserEntity recipient,
		final String status
	) {
		var friendRequest = new FriendRequestEntity();
		friendRequest.setSender(sender);
		friendRequest.setRecipient(recipient);
		friendRequest.setStatus(status);
		return friendRequest;
	}

	private UserEntity saveUser(final String username, final String email) {
		var user = new UserEntity();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword("password");
		return userRepository.save(user);
	}
}
