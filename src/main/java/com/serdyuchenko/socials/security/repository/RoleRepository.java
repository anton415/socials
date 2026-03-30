package com.serdyuchenko.socials.security.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.serdyuchenko.socials.security.models.ERole;
import com.serdyuchenko.socials.security.models.Role;

import java.util.Optional;

/**
 * Репозиторий для получения роли по ее enum-значению.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
