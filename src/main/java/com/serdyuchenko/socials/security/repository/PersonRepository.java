package com.serdyuchenko.socials.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.serdyuchenko.socials.security.models.Person;

import java.util.Optional;

/**
 * Репозиторий для поиска и проверки уникальности пользователей security-модуля.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
