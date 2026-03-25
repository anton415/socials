package com.serdyuchenko.socials.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.serdyuchenko.socials.entity.UserEntity;

// Репозиторий пользователей.
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
	@Query("""
		select user
		from UserEntity user
		where user.username = :username
			and user.password = :password
		""")
	Optional<UserEntity> findByUsernameAndPassword(
		@Param("username") String username,
		@Param("password") String password
	);
}
