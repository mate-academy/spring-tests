package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final PasswordValidator passwordValidator = new PasswordValidator();
    private static final ConstraintValidatorContext constraintValidatorContext =
            Mockito.mock(ConstraintValidatorContext.class);
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        passwordValidator.initialize(UserRegistrationDto.class.getAnnotation(Password.class));
        userRegistrationDto = new UserRegistrationDto();
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setPassword("qwerty1234");
        userRegistrationDto.setRepeatPassword("qwerty1234");
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_DifferentPasswords_NotOk() {
        userRegistrationDto.setPassword("qwerty1234");
        userRegistrationDto.setRepeatPassword("");
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
        userRegistrationDto.setPassword("");
        userRegistrationDto.setRepeatPassword("qwerty1234");
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_NullPassword_NotOk() {
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(null);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
