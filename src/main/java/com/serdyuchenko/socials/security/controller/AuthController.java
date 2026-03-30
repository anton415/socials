package com.serdyuchenko.socials.security.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.serdyuchenko.socials.security.dtos.request.LoginRequestDTO;
import com.serdyuchenko.socials.security.dtos.request.SignupRequestDTO;
import com.serdyuchenko.socials.security.dtos.response.JwtResponseDTO;
import com.serdyuchenko.socials.security.dtos.response.MessageResponseDTO;
import com.serdyuchenko.socials.security.dtos.response.RegisterDTO;
import com.serdyuchenko.socials.security.jwt.JwtUtils;
import com.serdyuchenko.socials.security.services.PersonService;
import com.serdyuchenko.socials.security.userdetails.UserDetailsImpl;

import java.util.List;

/**
 * Контроллер регистрации и входа пользователя.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private PersonService personService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Регистрирует нового пользователя и возвращает результат операции.
     *
     * @param signUpRequest данные для регистрации
     * @return ответ с текстом результата регистрации
     */
    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDTO> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequest) {
        RegisterDTO registerDTO = personService.signUp(signUpRequest);
        return ResponseEntity.status(registerDTO.getStatus())
                .body(new MessageResponseDTO(registerDTO.getMessage()));
    }

    /**
     * Проверяет логин и пароль пользователя, затем выдает JWT и список его ролей.
     *
     * @param loginRequestDTO данные для входа
     * @return ответ с JWT и сведениями о пользователе
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return ResponseEntity
                .ok(new JwtResponseDTO(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }
}
