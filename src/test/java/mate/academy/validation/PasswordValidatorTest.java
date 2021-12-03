package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.*;

import static org.junit.jupiter.api.Assertions.*;
class PasswordValidatorTest {
    private static ConstraintValidatorContext context;
    private static Password constraintAnnotation;
    private static PasswordValidator passwordValidator;
    private static UserRegistrationDto userRegistrationDto;

    @BeforeAll
    static void beforeAll() {
        passwordValidator = new PasswordValidator();
        constraintAnnotation = Mockito.mock(Password.class);
        context = Mockito.mock(ConstraintValidatorContext.class);
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("bob@i.ua");
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setPassword("123423");
        userRegistrationDto.setRepeatPassword("123423");
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        PasswordValidator passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
        boolean result = passwordValidator.isValid(userRegistrationDto, context);
        assertTrue(result);
    }

    @Test
    void isValid_PasswordsNotMatch() {
        userRegistrationDto.setRepeatPassword("21");
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        PasswordValidator passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
        boolean result = passwordValidator.isValid(userRegistrationDto, context);
        assertFalse(result);
    }

    @Test
    void isValid_NullPassword() {
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(null);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        PasswordValidator passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
        boolean result = passwordValidator.isValid(userRegistrationDto, context);
        assertFalse(result);
    }
}