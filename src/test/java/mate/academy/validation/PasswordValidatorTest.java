package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class PasswordValidatorTest {
    private static final String PASSWORD = "1221";
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        Password constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        userRegistrationDto = new UserRegistrationDto();
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword("not equal");
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
