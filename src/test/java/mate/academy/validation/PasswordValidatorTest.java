package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String PASSWORD = "12345678";
    private static final String REPEAT_WRIGHT_PASSWORD = "12345678";
    private static final String REPEAT_WRONG_PASSWORD = "12345677";
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
        userRegistrationDto.setRepeatPassword(REPEAT_WRIGHT_PASSWORD);
        Assertions.assertTrue(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    public void isValid_RepeatWrongPassword_NotOk() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(REPEAT_WRONG_PASSWORD);
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    public void isValid_NullUser_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> passwordValidator.isValid(null, constraintValidatorContext));
    }
}
