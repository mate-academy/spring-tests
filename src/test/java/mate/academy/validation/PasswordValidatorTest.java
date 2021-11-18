package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private UserRegistrationDto registrationDto;
    private ConstraintValidatorContext constraintValidatorContext;
    private Password constraintAnnotation;
    private static final String PASSWORD = "12345678";
    private static final String INCORRECT_PASSWORD = "1234";

    @BeforeEach
    void setUp() {
        registrationDto = new UserRegistrationDto();
        registrationDto.setPassword("12345678");
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_Ok() {
        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(PASSWORD);
        boolean actual = passwordValidator.isValid(registrationDto, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_NotOk() {
        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(INCORRECT_PASSWORD);
        boolean actual = passwordValidator.isValid(registrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}
