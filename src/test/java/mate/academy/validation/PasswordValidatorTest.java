package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class PasswordValidatorTest {
    private static ConstraintValidatorContext constraintValidatorContext;
    private static PasswordValidator passwordValidator;
    private static Password constraintAnnotation;
    private UserRegistrationDto dto;

    @BeforeAll
    static void beforeAll() {
        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
    }

    @BeforeEach
    void setUp() {
        dto = new UserRegistrationDto();
        dto.setPassword("12345678");
    }

    @Test
    void isValid_Ok() {
        dto.setRepeatPassword("12345678");
        Assertions.assertTrue(passwordValidator.isValid(dto, constraintValidatorContext));
    }

    @Test
    void isValid_fieldsDoNotMatch_notOk() {
        dto.setRepeatPassword("123456789");
        Assertions.assertFalse(passwordValidator.isValid(dto, constraintValidatorContext));
    }

    @Test
    void isValid_nullPassword_notOk() {
        dto.setPassword(null);
        dto.setRepeatPassword("12345678");
        Assertions.assertFalse(passwordValidator.isValid(dto, constraintValidatorContext));
    }

    @Test
    void isValid_nullRepeatPassword_notOk() {
        Assertions.assertFalse(passwordValidator.isValid(dto, constraintValidatorContext));
    }

    @Test
    void isValid_nullBothFields_notOk() {
        dto.setPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(dto, constraintValidatorContext));
    }
}
