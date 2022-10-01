package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String USER_EMAIL = "bchupika@mate.academy";
    private static final String USER_PASSWORD = "12345678";
    private static final String NOT_VALID_REPEAT_PASSWORD = "87654321";
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
    void isValid_NotOk_False() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                Mockito.mock(ConstraintValidatorContext.class)));
        registrationDto.setPassword(USER_PASSWORD);
        registrationDto.setPassword(NOT_VALID_REPEAT_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                Mockito.mock(ConstraintValidatorContext.class)));
    }
}
