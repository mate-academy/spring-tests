package mate.academy.dao.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import mate.academy.validation.Password;
import mate.academy.validation.PasswordValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String USER_EMAIL = "bob@gmail.com";
    private static final String USER_PASSWORD = "1234";
    private static final String INVALID_REPEAT_PASSWORD = "12345";
    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        Password annotation = Mockito.mock(Password.class);
        passwordValidator = new PasswordValidator();
        Mockito.when(annotation.field()).thenReturn("password");
        Mockito.when(annotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(annotation);
    }

    @Test
    void isValid_Ok() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(USER_EMAIL);
        registrationDto.setPassword(USER_PASSWORD);
        registrationDto.setRepeatPassword(USER_PASSWORD);
        boolean actual = passwordValidator.isValid(registrationDto,
                Mockito.mock(ConstraintValidatorContext.class));
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_NotOk() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                Mockito.mock(ConstraintValidatorContext.class)));
        registrationDto.setPassword(USER_PASSWORD);
        registrationDto.setPassword(INVALID_REPEAT_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                Mockito.mock(ConstraintValidatorContext.class)));
    }
}
