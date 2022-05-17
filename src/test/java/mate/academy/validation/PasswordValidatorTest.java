package mate.academy.validation;

import java.util.List;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext context;
    private Password password;
    private static final String EMAIL = "Lily20@ya.ru";
    private static final String CORRECT_PASSWORDS = "QwE123";
    private static final String CORRECT_REPEAT_PASSWORD = "QwE123";
    private static final String INCORRECT_REPEAT_PASSWORD = "asd123";

    @BeforeEach
    void setUp() {
        password = Mockito.mock(Password.class);
        context = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();

        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");

        passwordValidator.initialize(password);
    }

    @Test
    void isValid_Ok() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail(EMAIL);
        dto.setPassword(CORRECT_PASSWORDS);
        dto.setRepeatPassword(CORRECT_REPEAT_PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(dto, context));
    }

    @Test
    void isValid_NotOk() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail(EMAIL);
        dto.setPassword(CORRECT_PASSWORDS);
        dto.setRepeatPassword(INCORRECT_REPEAT_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(dto, context));
    }
}
