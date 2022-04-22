package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String USER_EMAIL = "user@gmail.com";
    private static final String USER_PASSWORD = "12345";
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto registrationDto;
    private PasswordValidator passwordValidator;
    private Password constraintAnnotation;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(USER_EMAIL);
        registrationDto.setPassword(USER_PASSWORD);

        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_OK() {
        registrationDto.setRepeatPassword(USER_PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(
                registrationDto, constraintValidatorContext), "Passwords should match");
    }

    @Test
    void isValid_NotOk() {
        registrationDto.setRepeatPassword(USER_PASSWORD + "1");
        Assertions.assertFalse(passwordValidator.isValid(
                registrationDto, constraintValidatorContext), "Passwords should NOT match");
    }
}
