package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String EMAIL = "user@i.ua";
    private static final String VALID_PASSWORD = "user1234";
    private static final String INCORRECT_PASSWORD = "user12";

    private PasswordValidator passwordValidator;
    private UserRegistrationDto user;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
        user = new UserRegistrationDto();
        user.setEmail(EMAIL);
    }

    @Test
    void isValid_isSuccessful_ok() {
        user.setPassword(VALID_PASSWORD);
        user.setRepeatPassword(VALID_PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void isValid_passwordIsEmpty_notOk() {
        Assertions.assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void isValid_incorrectPassword_notOk() {
        user.setPassword(VALID_PASSWORD);
        user.setRepeatPassword(INCORRECT_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }
}
