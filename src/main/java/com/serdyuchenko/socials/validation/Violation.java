package com.serdyuchenko.socials.validation;

// Поле и сообщение о нарушении валидации.
public record Violation(String fieldName, String message) {
}
