package validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import mate.academy.validation.Password;
import mate.academy.validation.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private Password constraintAnnotation;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    public void isValid_Ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("12345678");
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    public void isValid_NotOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("1234567");
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("");
        userRegistrationDto.setRepeatPassword("");
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
