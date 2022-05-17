package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PasswordValidatorTest {
    private static final String PASSWORD = "bobTheBest";
    private static final String SECOND_PASSWORD = "bobNotTheBest";
    private UserRegistrationDto userDto;
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        userDto = new UserRegistrationDto();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValidMethod_Ok() {
        userDto.setPassword(PASSWORD);
        userDto.setRepeatPassword(PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(userDto, constraintValidatorContext));
    }

    @Test
    void isValid_NotOk() {
        userDto.setPassword(PASSWORD);
        userDto.setRepeatPassword(SECOND_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(userDto, constraintValidatorContext));
    }
}
