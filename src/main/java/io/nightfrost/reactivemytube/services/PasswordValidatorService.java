package io.nightfrost.reactivemytube.services;

import io.nightfrost.reactivemytube.exceptions.ValidationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PasswordValidatorService {

    public Mono<Boolean> validatePassword(String password) {
        return Mono.fromSupplier(() -> {
            // Password must be at least 8 characters long
            if (password.length() < 8) {
                throw new ValidationException("Password must be longer than 8 characters.");
            }

            // Check for at least one uppercase letter
            if (!password.matches(".*[A-Z].*")) {
                throw new ValidationException("Password must contain at least one(1) uppercase letter.(A-Z)");
            }

            // Check for at least one lowercase letter
            if (!password.matches(".*[a-z].*")) {
                throw new ValidationException("Password must contain at least one(1) lowercase letter.(a-z)");
            }

            // Check for at least one digit
            if (!password.matches(".*\\d.*")) {
                throw new ValidationException("Password must contain at least one(1) digit.(1, 2, 3, 4...)");
            }

            // Check for at least one special character
            if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
                throw new ValidationException("Password must contain at least one(1) special character.(!, @, #, %...)");
            }

            return true;
        });
    }
}
