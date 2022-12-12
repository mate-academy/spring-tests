package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator = new PasswordValidator();
    private Password passwordAnnotation;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordAnnotation = Mockito.mock(Password.class);
        Mockito.when(passwordAnnotation.field()).thenReturn("password");
        Mockito.when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
    }

    @Test
    void isValid_checkPasswordsMatching_Ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("12345678");
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_checkLength_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword("1234");
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_passwordsDoNotMatches_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("1234abcd");
        userRegistrationDto.setRepeatPassword("1234ABCD");
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_checkPasswordIsEmpty_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setRepeatPassword("12345678");
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
