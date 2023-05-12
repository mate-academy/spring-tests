package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

public class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext context;
    private UserRegistrationDto userRegistrationDto;
    private Password password;

    @BeforeEach
    void setup() {
        passwordValidator = new PasswordValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("password123");
        userRegistrationDto.setRepeatPassword("password123");
        password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn(userRegistrationDto.getPassword());
        Mockito.when(password.fieldMatch()).thenReturn(userRegistrationDto.getRepeatPassword());
        passwordValidator.initialize(password);
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto, context),
                "Expected valid passwords");
    }
    @Test
    public void testInvalidPasswords() {
        userRegistrationDto.setRepeatPassword("differentPassword");
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, context),
                "Expected invalid passwords");
    }

    @Test
    public void testNullPassword() {
        userRegistrationDto.setPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, context),
                "Expected invalid passwords");
    }
}
