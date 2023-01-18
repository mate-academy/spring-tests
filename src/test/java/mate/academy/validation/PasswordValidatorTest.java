package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private UserRegistrationDto user;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        user = new UserRegistrationDto();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
        user.setEmail("bob@gmail.com");
    }

    @Test
    void isValid_Ok() {
        user.setPassword("123456");
        user.setRepeatPassword("123456");
        assertTrue(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void isValid_emptyPassword_NotOk() {
        assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void isValid_differentPasswords_NotOk() {
        user.setPassword("123");
        user.setRepeatPassword("456");
        assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }
}
