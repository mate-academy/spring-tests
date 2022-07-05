package mate.academy.validation;

import mate.academy.model.User;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

class PasswordValidatorTest {
    private final String EMAIL = "vitalii@gmail.com";
    private static final String PASSWORD = "1234";
    private static final String REPEAT_PASSWORD = "1234";
    private static final String INVALID_PASSWORD = "12345678";
    private UserRegistrationDto registrationDto;
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        registrationDto = new UserRegistrationDto();
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_OK() {
        registrationDto.setEmail(EMAIL);
        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(REPEAT_PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_differentPasswords_notOk() {
        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(INVALID_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));
        registrationDto.setPassword(INVALID_PASSWORD);
        registrationDto.setRepeatPassword(PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));
        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));
    }
}