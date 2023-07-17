package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PasswordValidatorTest {
    private ConstraintValidatorContext constraintValidatorContext;
    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = mock(ConstraintValidatorContext.class);
        Password passwordAnnotation = mock(Password.class);
        when(passwordAnnotation.field()).thenReturn("password");
        when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
    }

    @Test
    void validatePassword_Ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("12345");
        userRegistrationDto.setRepeatPassword("12345");
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void validatePassword_FieldsNotMatch_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("12345");
        userRegistrationDto.setRepeatPassword("54321");
        Assertions.assertFalse(
                passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
