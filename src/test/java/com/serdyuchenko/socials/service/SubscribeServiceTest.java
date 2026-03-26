package com.serdyuchenko.socials.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.serdyuchenko.socials.entity.FriendshipEntity;
import com.serdyuchenko.socials.entity.FriendshipId;
import com.serdyuchenko.socials.entity.SubscriptionEntity;
import com.serdyuchenko.socials.entity.SubscriptionId;
import com.serdyuchenko.socials.entity.UserEntity;
import com.serdyuchenko.socials.repository.AbstractRepositoryTest;
import com.serdyuchenko.socials.repository.FriendRequestRepository;
import com.serdyuchenko.socials.repository.FriendshipRepository;
import com.serdyuchenko.socials.repository.SubscriptionRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubscribeServiceTest extends AbstractRepositoryTest {

	@Autowired
	private SubscribeService subscribeService;

	@Autowired
	private FriendRequestRepository friendRequestRepository;

	@Autowired
	private FriendshipRepository friendshipRepository;

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@BeforeEach
	public void setUp() {
		cleanDatabase();
	}

	@Test
	@DisplayName("При отправке заявки отправитель становится подписчиком")
	public void whenSendFriendRequestThenSenderShouldFollowRecipient() {
		var sender = saveUser("sender", "sender@example.com");
		var recipient = saveUser("recipient", "recipient@example.com");

		var friendRequest = subscribeService.sendFriendRequest(sender.getId(), recipient.getId());

		var foundRequest = friendRequestRepository.findById(friendRequest.getId());
		var senderSubscriptionId = new SubscriptionId(sender.getId(), recipient.getId());

		assertThat(foundRequest).isPresent();
		assertThat(foundRequest.get().getStatus()).isEqualTo("PENDING");
		assertThat(subscriptionRepository.existsById(senderSubscriptionId)).isTrue();
	}

	@Test
	@DisplayName("При принятии заявки пользователи становятся друзьями и взаимными подписчиками")
	public void whenAcceptFriendRequestThenUsersShouldBecomeFriendsAndMutualFollowers() {
		var sender = saveUser("sender", "sender@example.com");
		var recipient = saveUser("recipient", "recipient@example.com");
		var friendRequest = subscribeService.sendFriendRequest(sender.getId(), recipient.getId());

		subscribeService.acceptFriendRequest(friendRequest.getId(), recipient.getId());

		var updatedRequest = friendRequestRepository.findById(friendRequest.getId());
		assertThat(updatedRequest).isPresent();
		assertThat(updatedRequest.get().getStatus()).isEqualTo("ACCEPTED");
		assertThat(friendshipRepository.existsById(new FriendshipId(sender.getId(), recipient.getId()))).isTrue();
		assertThat(friendshipRepository.existsById(new FriendshipId(recipient.getId(), sender.getId()))).isTrue();
		assertThat(subscriptionRepository.existsById(new SubscriptionId(sender.getId(), recipient.getId()))).isTrue();
		assertThat(subscriptionRepository.existsById(new SubscriptionId(recipient.getId(), sender.getId()))).isTrue();
	}

	@Test
	@DisplayName("При отклонении заявки отправитель остается подписчиком")
	public void whenRejectFriendRequestThenSenderShouldRemainFollower() {
		var sender = saveUser("sender", "sender@example.com");
		var recipient = saveUser("recipient", "recipient@example.com");
		var friendRequest = subscribeService.sendFriendRequest(sender.getId(), recipient.getId());

		subscribeService.rejectFriendRequest(friendRequest.getId(), recipient.getId());

		var updatedRequest = friendRequestRepository.findById(friendRequest.getId());
		assertThat(updatedRequest).isPresent();
		assertThat(updatedRequest.get().getStatus()).isEqualTo("REJECTED");
		assertThat(subscriptionRepository.existsById(new SubscriptionId(sender.getId(), recipient.getId()))).isTrue();
		assertThat(friendshipRepository.existsById(new FriendshipId(sender.getId(), recipient.getId()))).isFalse();
		assertThat(friendshipRepository.existsById(new FriendshipId(recipient.getId(), sender.getId()))).isFalse();
	}

	@Test
	@DisplayName("При удалении из друзей удаляется только своя подписка")
	public void whenRemoveFriendThenInitiatorShouldUnfollowOnlyFromOwnSide() {
		var firstUser = saveUser("first-user", "first-user@example.com");
		var secondUser = saveUser("second-user", "second-user@example.com");
		friendshipRepository.save(createFriendship(firstUser, secondUser));
		friendshipRepository.save(createFriendship(secondUser, firstUser));
		subscriptionRepository.save(createSubscription(firstUser, secondUser));
		subscriptionRepository.save(createSubscription(secondUser, firstUser));

		subscribeService.removeFriend(firstUser.getId(), secondUser.getId());

		assertThat(friendshipRepository.existsById(new FriendshipId(firstUser.getId(), secondUser.getId())))
			.isFalse();
		assertThat(friendshipRepository.existsById(new FriendshipId(secondUser.getId(), firstUser.getId())))
			.isTrue();
		assertThat(subscriptionRepository.existsById(new SubscriptionId(firstUser.getId(), secondUser.getId())))
			.isFalse();
		assertThat(subscriptionRepository.existsById(new SubscriptionId(secondUser.getId(), firstUser.getId())))
			.isTrue();
	}

	private FriendshipEntity createFriendship(final UserEntity user, final UserEntity friend) {
		var friendship = new FriendshipEntity();
		friendship.setUser(user);
		friendship.setFriend(friend);
		return friendship;
	}

	private SubscriptionEntity createSubscription(final UserEntity follower, final UserEntity followee) {
		var subscription = new SubscriptionEntity();
		subscription.setFollower(follower);
		subscription.setFollowee(followee);
		return subscription;
	}

	private UserEntity saveUser(final String username, final String email) {
		var user = new UserEntity();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword("password");
		return userRepository.save(user);
	}
}
