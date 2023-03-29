package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String USER_EMAIL = "bchupika@mate.academy";
    private static final String USER_PASSWORD = "12345678";
    private static final String WRONG_REPEAT_PASSWORD = "11111111";
    private static final String EMPTY_PASSWORD = "";
    private PasswordValidator passwordValidator;
    private Password constraintAnnotation;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        constraintAnnotation = Mockito.mock(Password.class);

        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);

        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(USER_EMAIL);
        userRegistrationDto.setPassword(USER_PASSWORD);
        userRegistrationDto.setRepeatPassword(USER_PASSWORD);
    }

    @Test
    void isValid_ValidData_Ok() {
        Assertions.assertTrue(passwordValidator.isValid(
                userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_PasswordsDoNotMatch_NotOk() {
        userRegistrationDto.setRepeatPassword(WRONG_REPEAT_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(
                userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_EmptyOrNullValuedPassword_NotOk() {
        userRegistrationDto.setPassword(EMPTY_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(
                userRegistrationDto, constraintValidatorContext));
        userRegistrationDto.setPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(
                userRegistrationDto, constraintValidatorContext));
    }
}
