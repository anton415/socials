package com.serdyuchenko.socials.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.serdyuchenko.socials.entity.ImageEntity;

// Репозиторий изображений.
public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
	@Modifying
	@Transactional
	@Query("""
		delete from ImageEntity image
		where image.post.id = :postId
		""")
	int deleteAllByPostId(@Param("postId") UUID postId);
}
