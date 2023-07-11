package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PasswordValidatorTest {
    private ConstraintValidatorContext constraintValidatorContext;
    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password passwordAnnotation = Mockito.mock(Password.class);
        Mockito.when(passwordAnnotation.field()).thenReturn("password");
        Mockito.when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
    }

    @Test
    void validatePassword_ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("12345");
        userRegistrationDto.setRepeatPassword("12345");
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void validatePassword_FieldsNotMatch_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("12345");
        userRegistrationDto.setRepeatPassword("54321");
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}
