package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PasswordValidatorTest {
    private static final String CORRECT_PASSWORD = "validPassword2023";
    private static final String INCORRECT_PASSWORD = "IncorrectPassword1111";
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext context;
    private UserRegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
        registrationDto = new UserRegistrationDto();
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
    }

    @Test
    void isValid_ok() {
        registrationDto.setPassword(CORRECT_PASSWORD);
        registrationDto.setRepeatPassword(CORRECT_PASSWORD);
        boolean isValid = passwordValidator.isValid(registrationDto, context);
        Assertions.assertTrue(isValid);
    }

    @Test
    void isValid_mismatchedPasswords_notOk() {
        registrationDto.setPassword(CORRECT_PASSWORD);
        registrationDto.setRepeatPassword(INCORRECT_PASSWORD);
        boolean isValid = passwordValidator.isValid(registrationDto, context);
        Assertions.assertFalse(isValid);
    }

    @Test
    void isValid_passwordIsNull_notOk() {
        registrationDto.setPassword(null);
        registrationDto.setRepeatPassword(CORRECT_PASSWORD);
        boolean isValid = passwordValidator.isValid(registrationDto, context);
        Assertions.assertFalse(isValid);
    }

    @Test
    void isValid_bothPasswordsNull_notOk() {
        registrationDto.setPassword(null);
        registrationDto.setRepeatPassword(null);
        boolean isValid = passwordValidator.isValid(registrationDto, context);
        Assertions.assertFalse(isValid);
    }
}
