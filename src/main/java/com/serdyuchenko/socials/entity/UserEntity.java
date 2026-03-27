package com.serdyuchenko.socials.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Сущность пользователя.
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotBlank(message = "username не может быть пустым")
	@Column(nullable = false, unique = true)
	private String username;

	@NotBlank(message = "email не может быть пустым")
	@Email(message = "email имеет некорректный формат")
	@Column(nullable = false, unique = true)
	private String email;

	@NotBlank(message = "password не может быть пустым")
	@Column(nullable = false)
	private String password;
}
