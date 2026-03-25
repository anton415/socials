package com.serdyuchenko.socials.repository;

import org.springframework.beans.factory.annotation.Autowired;

// Общая очистка данных для репозиториев
public abstract class AbstractRepositoryTest {

	@Autowired
	private FriendRequestRepository friendRequestRepository;

	@Autowired
	private FriendshipRepository friendshipRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	protected UserRepository userRepository;

	protected void cleanDatabase() {
		imageRepository.deleteAll();
		messageRepository.deleteAll();
		friendRequestRepository.deleteAll();
		friendshipRepository.deleteAll();
		subscriptionRepository.deleteAll();
		postRepository.deleteAll();
		userRepository.deleteAll();
	}
}
