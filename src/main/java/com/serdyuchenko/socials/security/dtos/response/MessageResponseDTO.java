package com.serdyuchenko.socials.security.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Универсальный DTO для короткого текстового сообщения клиенту.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {
    private String message;
}
