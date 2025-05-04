package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static final String PASSWORD = "0123456789";
    private static UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(PASSWORD);
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);

    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setRepeatPassword(PASSWORD);
        Assertions.assertTrue(passwordValidator
                .isValid(userRegistrationDto,constraintValidatorContext));
    }

    @Test
    void isValid_NotOk() {
        userRegistrationDto.setRepeatPassword(PASSWORD + "A");
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto,constraintValidatorContext));
    }
}
