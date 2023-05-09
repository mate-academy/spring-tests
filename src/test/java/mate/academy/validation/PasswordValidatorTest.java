package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String USER_EMAIL = "john@me.com";
    private static final String USER_PASSWORD = "12345678";
    private static final String MISMATCH_PASSWORD = "123456789";
    private static final String SHORT_PASSWORD = "123";
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext validatorContext;
    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        Password constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field())
                .thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch())
                .thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);

        validatorContext = Mockito.mock(ConstraintValidatorContext.class);

        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(USER_EMAIL);
        userRegistrationDto.setPassword(USER_PASSWORD);
        userRegistrationDto.setRepeatPassword(USER_PASSWORD);
    }

    @Test
    void isValid_validData_Ok() {
        Assertions.assertTrue(passwordValidator.isValid(
                userRegistrationDto, validatorContext));
    }

    @Test
    void isValid_mismatchPasswords_notOk() {
        userRegistrationDto.setPassword(MISMATCH_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(
                userRegistrationDto, validatorContext));
    }

    @Test
    void isValid_invalidPassword_notOk() {
        userRegistrationDto.setPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(
                userRegistrationDto, validatorContext));
        userRegistrationDto.setPassword("");
        Assertions.assertFalse(passwordValidator.isValid(
                userRegistrationDto, validatorContext));
        userRegistrationDto.setPassword(SHORT_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(
                userRegistrationDto, validatorContext));
    }
}
