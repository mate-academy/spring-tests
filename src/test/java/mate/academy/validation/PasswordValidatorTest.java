package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
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
        user.setEmail("bob@i.ua");
    }

    @Test
    void isValid_ok() {
        user.setPassword("12345");
        user.setRepeatPassword("12345");
        Assertions.assertTrue(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void isValid_emptyPassword_NotOk() {
        Assertions.assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void isValid_incorrectPassword_notOk() {
        user.setPassword("12345");
        user.setRepeatPassword("123456");
        Assertions.assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }
}
