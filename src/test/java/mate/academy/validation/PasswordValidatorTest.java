package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD_VALID = "12345678";
    private static final String PASSWORD_NOT_VALID = "123456789";
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
        user.setEmail(EMAIL);
    }

    @Test
    void isValid_Ok() {
        user.setPassword(PASSWORD_VALID);
        user.setRepeatPassword(PASSWORD_VALID);
        Assertions.assertTrue(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void isValid_emptyPassword_NotOk() {
        Assertions.assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void isValid_incorrectPassword_notOk() {
        user.setPassword(PASSWORD_VALID);
        user.setRepeatPassword(PASSWORD_NOT_VALID);
        Assertions.assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }
}
