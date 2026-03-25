package com.serdyuchenko.socials.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.serdyuchenko.socials.entity.SubscriptionEntity;
import com.serdyuchenko.socials.entity.SubscriptionId;
import com.serdyuchenko.socials.entity.UserEntity;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubscriptionRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	// Очищает данные
	@BeforeEach
	public void setUp() {
		cleanDatabase();
	}

	@Test
	@DisplayName("Должен сохранять подписку и находить по составному ключу")
	public void whenSaveSubscriptionThenFindById() {
		var follower = saveUser("follower", "follower@example.com");
		var followee = saveUser("followee", "followee@example.com");
		var subscription = new SubscriptionEntity();
		subscription.setFollower(follower);
		subscription.setFollowee(followee);
		var savedSubscription = subscriptionRepository.save(subscription);

		var foundSubscription = subscriptionRepository.findById(savedSubscription.getId());

		assertThat(foundSubscription).isPresent();
		assertThat(foundSubscription.get().getId())
			.isEqualTo(new SubscriptionId(follower.getId(), followee.getId()));
	}

	@Test
	@DisplayName("Должен возвращать все сохраненные подписки")
	public void whenFindAllThenReturnAllSubscriptions() {
		var follower = saveUser("follower", "follower@example.com");
		var firstFollowee = saveUser("first-followee", "first.followee@example.com");
		var secondFollowee = saveUser("second-followee", "second.followee@example.com");
		subscriptionRepository.save(createSubscription(follower, firstFollowee));
		subscriptionRepository.save(createSubscription(follower, secondFollowee));

		var subscriptions = subscriptionRepository.findAll();

		assertThat(subscriptions).hasSize(2);
		assertThat(subscriptions)
			.extracting(SubscriptionEntity::getId)
			.containsExactlyInAnyOrder(
				new SubscriptionId(follower.getId(), firstFollowee.getId()),
				new SubscriptionId(follower.getId(), secondFollowee.getId())
			);
	}

	// Создает подписку
	private SubscriptionEntity createSubscription(
		final UserEntity follower,
		final UserEntity followee
	) {
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
