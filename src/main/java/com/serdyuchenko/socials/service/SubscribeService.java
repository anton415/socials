package com.serdyuchenko.socials.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.serdyuchenko.socials.entity.FriendRequestEntity;
import com.serdyuchenko.socials.entity.FriendshipEntity;
import com.serdyuchenko.socials.entity.FriendshipId;
import com.serdyuchenko.socials.entity.SubscriptionEntity;
import com.serdyuchenko.socials.entity.SubscriptionId;
import com.serdyuchenko.socials.entity.UserEntity;
import com.serdyuchenko.socials.repository.FriendRequestRepository;
import com.serdyuchenko.socials.repository.FriendshipRepository;
import com.serdyuchenko.socials.repository.SubscriptionRepository;
import com.serdyuchenko.socials.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Сервис для управления подписками и дружбой.
 */
@Service
@RequiredArgsConstructor
public class SubscribeService {

	private static final String REQUEST_STATUS_PENDING = "PENDING";

	private static final String REQUEST_STATUS_ACCEPTED = "ACCEPTED";

	private static final String REQUEST_STATUS_REJECTED = "REJECTED";

	private final UserRepository userRepository;

	private final SubscriptionRepository subscriptionRepository;

	private final FriendRequestRepository friendRequestRepository;

	private final FriendshipRepository friendshipRepository;

	@Transactional
	public FriendRequestEntity sendFriendRequest(final UUID senderId, final UUID recipientId) {
		validateDifferentUsers(senderId, recipientId);
		var sender = findUser(senderId);
		var recipient = findUser(recipientId);
		var friendRequest = new FriendRequestEntity();
		friendRequest.setSender(sender);
		friendRequest.setRecipient(recipient);
		friendRequest.setStatus(REQUEST_STATUS_PENDING);
		subscribeIfAbsent(sender, recipient);
		return friendRequestRepository.save(friendRequest);
	}

	@Transactional
	public FriendRequestEntity acceptFriendRequest(final UUID requestId) {
		var friendRequest = findFriendRequest(requestId);
		friendRequest.setStatus(REQUEST_STATUS_ACCEPTED);
		addFriendshipAndMutualSubscription(friendRequest.getSender(), friendRequest.getRecipient());
		return friendRequestRepository.save(friendRequest);
	}

	@Transactional
	public FriendRequestEntity acceptFriendRequest(final UUID requestId, final UUID recipientId) {
		var friendRequest = findFriendRequestForRecipient(requestId, recipientId);
		friendRequest.setStatus(REQUEST_STATUS_ACCEPTED);
		addFriendshipAndMutualSubscription(friendRequest.getSender(), friendRequest.getRecipient());
		return friendRequestRepository.save(friendRequest);
	}

	@Transactional
	public FriendRequestEntity rejectFriendRequest(final UUID requestId) {
		var friendRequest = findFriendRequest(requestId);
		friendRequest.setStatus(REQUEST_STATUS_REJECTED);
		return friendRequestRepository.save(friendRequest);
	}

	@Transactional
	public FriendRequestEntity rejectFriendRequest(final UUID requestId, final UUID recipientId) {
		var friendRequest = findFriendRequestForRecipient(requestId, recipientId);
		friendRequest.setStatus(REQUEST_STATUS_REJECTED);
		return friendRequestRepository.save(friendRequest);
	}

	@Transactional
	public void removeFriend(final UUID userId, final UUID friendId) {
		validateDifferentUsers(userId, friendId);
		friendshipRepository.deleteById(new FriendshipId(userId, friendId));
		unsubscribe(userId, friendId);
	}

	@Transactional
	public void unsubscribe(final UUID followerId, final UUID followeeId) {
		validateDifferentUsers(followerId, followeeId);
		subscriptionRepository.deleteById(new SubscriptionId(followerId, followeeId));
	}

	private void addFriendshipAndMutualSubscription(final UserEntity firstUser, final UserEntity secondUser) {
		addFriendshipIfAbsent(firstUser, secondUser);
		addFriendshipIfAbsent(secondUser, firstUser);
		subscribeIfAbsent(firstUser, secondUser);
		subscribeIfAbsent(secondUser, firstUser);
	}

	private void addFriendshipIfAbsent(final UserEntity user, final UserEntity friend) {
		var friendshipId = new FriendshipId(user.getId(), friend.getId());
		if (friendshipRepository.existsById(friendshipId)) {
			return;
		}
		var friendship = new FriendshipEntity();
		friendship.setUser(user);
		friendship.setFriend(friend);
		friendshipRepository.save(friendship);
	}

	private void subscribeIfAbsent(final UserEntity follower, final UserEntity followee) {
		var subscriptionId = new SubscriptionId(follower.getId(), followee.getId());
		if (subscriptionRepository.existsById(subscriptionId)) {
			return;
		}
		var subscription = new SubscriptionEntity();
		subscription.setFollower(follower);
		subscription.setFollowee(followee);
		subscriptionRepository.save(subscription);
	}

	private FriendRequestEntity findFriendRequestForRecipient(final UUID requestId, final UUID recipientId) {
		var friendRequest = findFriendRequest(requestId);
		if (!friendRequest.getRecipient().getId().equals(recipientId)) {
			throw new IllegalArgumentException("Заявка не принадлежит получателю");
		}
		return friendRequest;
	}

	private FriendRequestEntity findFriendRequest(final UUID requestId) {
		return friendRequestRepository.findById(requestId)
			.orElseThrow(() -> new IllegalArgumentException("Заявка в друзья не найдена: " + requestId));
	}

	private UserEntity findUser(final UUID userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + userId));
	}

	private void validateDifferentUsers(final UUID firstUserId, final UUID secondUserId) {
		if (firstUserId.equals(secondUserId)) {
			throw new IllegalArgumentException("Пользователь не может взаимодействовать сам с собой");
		}
	}
}
