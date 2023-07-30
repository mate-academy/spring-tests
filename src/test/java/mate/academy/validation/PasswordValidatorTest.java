package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PasswordValidatorTest {
    private static final String TEST_EMAIL = "testEmail@gmail.com";
    private static final String TEST_PASSWORD = "12345";
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
        user.setEmail(TEST_EMAIL);
    }

    @Test
    void validatePassword_PasswordIsValid_Ok() {
        user.setPassword(TEST_PASSWORD);
        user.setRepeatPassword(TEST_PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(user, constraintValidatorContext),
                "The actual password doesn't match the expected data");
    }

    @Test
    void validatePassword_emptyPassword_NotOk() {
        Assertions.assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void validatePassword_incorrectPassword_notOk() {
        user.setPassword(TEST_PASSWORD);
        user.setRepeatPassword("123456");
        Assertions.assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }
}
