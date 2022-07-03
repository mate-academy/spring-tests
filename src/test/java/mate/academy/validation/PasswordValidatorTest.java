package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("12345678");
        assertTrue(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void is_Not_Valid_Not_Ok() {
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("02345678");
        assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void password_Is_Null_Not_Ok() {
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(null);
        assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword("02345678");
        assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword(null);
        assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }
}
