package com.serdyuchenko.socials.security.userdetails;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.serdyuchenko.socials.security.models.Person;
import com.serdyuchenko.socials.security.repository.PersonRepository;

/**
 * Загружает пользователя из базы при попытке аутентификации.
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    PersonRepository personRepository;

    /**
     * Ищет пользователя по username и подготавливает его для Spring Security.
     *
     * @param username логин пользователя
     * @return найденный пользователь в виде UserDetails
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(person);
    }

}
