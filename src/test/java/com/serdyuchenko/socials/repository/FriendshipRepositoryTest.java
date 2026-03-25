package com.serdyuchenko.socials.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.serdyuchenko.socials.entity.FriendshipEntity;
import com.serdyuchenko.socials.entity.FriendshipId;
import com.serdyuchenko.socials.entity.UserEntity;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FriendshipRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	private FriendshipRepository friendshipRepository;

	@BeforeEach
	public void setUp() {
		cleanDatabase();
	}

	@Test
	@DisplayName("Должен сохранять дружбу и находить по составному ключу")
	public void whenSaveFriendshipThenFindById() {
		var user = saveUser("john", "john@example.com");
		var friend = saveUser("jane", "jane@example.com");
		var friendship = new FriendshipEntity();
		friendship.setUser(user);
		friendship.setFriend(friend);
		var savedFriendship = friendshipRepository.save(friendship);

		var foundFriendship = friendshipRepository.findById(savedFriendship.getId());

		assertThat(foundFriendship).isPresent();
		assertThat(foundFriendship.get().getId())
			.isEqualTo(new FriendshipId(user.getId(), friend.getId()));
	}

	@Test
	@DisplayName("Должен возвращать все сохраненные связи дружбы")
	public void whenFindAllThenReturnAllFriendships() {
		var user = saveUser("john", "john@example.com");
		var firstFriend = saveUser("jane", "jane@example.com");
		var secondFriend = saveUser("alex", "alex@example.com");
		friendshipRepository.save(createFriendship(user, firstFriend));
		friendshipRepository.save(createFriendship(user, secondFriend));

		var friendships = friendshipRepository.findAll();

		assertThat(friendships).hasSize(2);
		assertThat(friendships)
			.extracting(FriendshipEntity::getId)
			.containsExactlyInAnyOrder(
				new FriendshipId(user.getId(), firstFriend.getId()),
				new FriendshipId(user.getId(), secondFriend.getId())
			);
	}

	private FriendshipEntity createFriendship(final UserEntity user, final UserEntity friend) {
		var friendship = new FriendshipEntity();
		friendship.setUser(user);
		friendship.setFriend(friend);
		return friendship;
	}

	private UserEntity saveUser(final String username, final String email) {
		var user = new UserEntity();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword("password");
		return userRepository.save(user);
	}
}
