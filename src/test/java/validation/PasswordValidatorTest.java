package validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import mate.academy.validation.Password;
import mate.academy.validation.PasswordValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PasswordValidatorTest {
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext validatorContext;
    private static Password constraintAnnotation;

    @BeforeAll
    static void beforeAll() {
        validatorContext = Mockito.mock(ConstraintValidatorContext.class);
        constraintAnnotation = Mockito.mock(Password.class);
        passwordValidator = new PasswordValidator();
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
    }

    @Test
    void isValid_Ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("password");
        userRegistrationDto.setRepeatPassword("password");
        passwordValidator.initialize(constraintAnnotation);
        assertTrue(passwordValidator.isValid(userRegistrationDto, validatorContext));
    }

    @Test
    void isValid_wrongRepeatPassword_Ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("password");
        userRegistrationDto.setRepeatPassword("password1234");
        passwordValidator.initialize(constraintAnnotation);
        assertFalse(passwordValidator.isValid(userRegistrationDto, validatorContext));
    }

    @Test
    void isValid_nullPassword_Ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(null);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
        assertFalse(passwordValidator.isValid(userRegistrationDto, validatorContext));
    }
}
