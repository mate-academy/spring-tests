package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String PASSWORD = "password";
    private Password password;
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        password = Mockito.mock(Password.class);
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        userRegistrationDto = new UserRegistrationDto();
        passwordValidator = new PasswordValidator();
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);

    }

    @Test
    void isValid_ok() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_nullPassword_notOk() {
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_notEqualPasswords_notOk() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword("12345");
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }
}
