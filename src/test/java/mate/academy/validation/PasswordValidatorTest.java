package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
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
        UserRegistrationDto user = new UserRegistrationDto();
        user.setEmail("bob@i.ua");
        user.setPassword("bob341234");
        user.setRepeatPassword("bob341234");
        Assertions.assertTrue(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void isValid_Null() {
        UserRegistrationDto user = new UserRegistrationDto();
        user.setEmail("bob@i.ua");
        Assertions.assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }

    @Test
    void isValid_NotOk() {
        UserRegistrationDto user = new UserRegistrationDto();
        user.setEmail("bob@i.ua");
        user.setPassword("bob3412");
        user.setRepeatPassword("bob341234");
        Assertions.assertFalse(passwordValidator.isValid(user, constraintValidatorContext));
    }
}
