package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;
    private Password constraintAnnotation;
    private final String EMAIL = "bobik@g.com";
    private final String PASSWORD = "1234567890";
    private final String REPEAT_WRIGHT_PASSWORD = "1234567890";
    private final String REPEAT_WRONG_PASSWORD = "0987654321";

    @BeforeEach
    public void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    public void isValid_Ok() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(REPEAT_WRIGHT_PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    public void isValid_RepeatWrongPassword_NotOk() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(REPEAT_WRONG_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    public void isValid_NullUser_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> passwordValidator.isValid(null, constraintValidatorContext));
    }
}