package com.serdyuchenko.socials.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.serdyuchenko.socials.dto.UserCreateRequestDto;
import com.serdyuchenko.socials.dto.UserResponseDto;
import com.serdyuchenko.socials.dto.UserUpdateRequestDto;
import com.serdyuchenko.socials.entity.UserEntity;
import com.serdyuchenko.socials.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Сервис для управления пользователями.
 */
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public UserResponseDto createUser(final UserCreateRequestDto request) {
		var user = new UserEntity();
		user.setUsername(request.username());
		user.setEmail(request.email());
		user.setPassword(request.password());
		return toResponseDto(userRepository.save(user));
	}

	@Transactional(readOnly = true)
	public Optional<UserResponseDto> findById(final UUID userId) {
		return userRepository.findById(userId)
			.map(this::toResponseDto);
	}

	@Transactional
	public boolean updateUser(final UserUpdateRequestDto request) {
		var userOptional = userRepository.findById(request.id());
		if (userOptional.isEmpty()) {
			return false;
		}
		var user = userOptional.get();
		user.setUsername(request.username());
		user.setEmail(request.email());
		user.setPassword(request.password());
		userRepository.save(user);
		return true;
	}

	@Transactional
	public boolean deleteById(final UUID userId) {
		if (!userRepository.existsById(userId)) {
			return false;
		}
		userRepository.deleteById(userId);
		return true;
	}

	private UserResponseDto toResponseDto(final UserEntity user) {
		return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail());
	}
}
