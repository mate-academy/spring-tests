package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String PASSWORD = "12345";
    private static final String PASSWORD_REPEAT = "12345";
    private static final String WRONG_PASSWORD_REPEAT = "11111";
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;
    private Password constraintAnnotation;

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
        userRegistrationDto.setRepeatPassword(PASSWORD_REPEAT);
        Assertions.assertTrue(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    public void isValid_RepeatWrongPassword_NotOk() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(WRONG_PASSWORD_REPEAT);
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    public void isValid_NullUser_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> passwordValidator.isValid(null, constraintValidatorContext));
    }
}
