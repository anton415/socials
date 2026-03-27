package com.serdyuchenko.socials.validation;

import java.util.List;

// Ответ для неуспешной валидации.
public record ValidationErrorResponse(List<Violation> violations) {
}
