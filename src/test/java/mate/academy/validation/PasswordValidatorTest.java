package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "12345";
    private static final String INVALID_PASSWORD = "qwerty";
    private final PasswordValidator passwordValidator =
            new PasswordValidator();
    private final ConstraintValidatorContext constraintValidatorContext =
            mock(ConstraintValidatorContext.class);
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        try {
            Field field = passwordValidator.getClass().getDeclaredField("field");
            field.setAccessible(true);
            field.set(passwordValidator, "password");
            Field fieldMatch = passwordValidator.getClass().getDeclaredField("fieldMatch");
            fieldMatch.setAccessible(true);
            fieldMatch.set(passwordValidator, "repeatPassword");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
    }

    @Test
    void isValid_Ok() {
        assertTrue(
                passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_NotOk() {
        userRegistrationDto.setPassword(INVALID_PASSWORD);
        assertFalse(
                passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
