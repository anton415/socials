package com.serdyuchenko.socials.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.serdyuchenko.socials.entity.PostEntity;
import com.serdyuchenko.socials.entity.UserEntity;

// Репозиторий постов.
public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    // Список постов пользователя
    List<PostEntity> findAllByUser(UserEntity user);

    // Список постов в диапазоне даты
    List<PostEntity> findAllByCreatedBetween(OffsetDateTime start, OffsetDateTime end);

    // Список постов отсортированный по дате с пагинацией
    List<PostEntity> findAllByOrderByCreatedDesc(Pageable pageable);

	@Modifying
	@Transactional
	@Query("""
		update PostEntity post
		set post.title = :title,
			post.text = :text
		where post.id = :postId
		""")
	int updateTitleAndTextById(
		@Param("postId") UUID postId,
		@Param("title") String title,
		@Param("text") String text
	);

	@Modifying
	@Transactional
	@Query("""
		delete from PostEntity post
		where post.id = :postId
		""")
	int deletePostById(@Param("postId") UUID postId);

	@Query("""
		select post
		from PostEntity post
		where post.user.id in (
			select subscription.followee.id
			from SubscriptionEntity subscription
			where subscription.follower.id = :userId
		)
		order by post.created desc
		""")
	List<PostEntity> findSubscriberFeedByUserId(@Param("userId") UUID userId, Pageable pageable);
}
