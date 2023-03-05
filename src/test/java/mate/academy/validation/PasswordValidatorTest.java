package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PasswordValidatorTest {
    private static final String EMAIL = "bob@email.com";
    private static final String PASSWORD = "bob12345";
    private UserRegistrationDto user;
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
    }

    @Test
    void isValid_Ok() {
        user = new UserRegistrationDto();
        user.setPassword(PASSWORD);
        user.setRepeatPassword(PASSWORD);
        user.setEmail(EMAIL);
        Assertions.assertTrue(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void isValid_NotOk() {
        user = new UserRegistrationDto();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRepeatPassword("anotherPassword");
        Assertions.assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void isValid_Null() {
        user = new UserRegistrationDto();
        user.setEmail(EMAIL);
        Assertions.assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }
}
