package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import mate.academy.util.UserTestUtil;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
        userRegistrationDto = new UserRegistrationDto();
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setPassword(UserTestUtil.PASSWORD);
        userRegistrationDto.setRepeatPassword(UserTestUtil.PASSWORD);
        boolean actual = passwordValidator
                .isValid(userRegistrationDto, Mockito.mock(ConstraintValidatorContext.class));
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_NotOk() {
        userRegistrationDto.setPassword(UserTestUtil.PASSWORD);
        userRegistrationDto.setRepeatPassword("incorrect");
        boolean actual = passwordValidator
                .isValid(userRegistrationDto, Mockito.mock(ConstraintValidatorContext.class));
        Assertions.assertFalse(actual);
    }
}