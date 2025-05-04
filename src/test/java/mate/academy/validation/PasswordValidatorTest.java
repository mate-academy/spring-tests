package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "12345678";
    private static final String INVALID_PASSWORD = "0000";
    private static PasswordValidator passwordValidator;
    private static UserRegistrationDto registrationDto;
    private static ConstraintValidatorContext context;

    @BeforeAll
    static void beforeAll() {
        passwordValidator = new PasswordValidator();
        registrationDto = new UserRegistrationDto();
        context = Mockito.mock(ConstraintValidatorContext.class);
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
    }

    @Test
    void isValid_Ok() {
        registrationDto.setPassword(VALID_PASSWORD);
        registrationDto.setRepeatPassword(VALID_PASSWORD);
        assertTrue(passwordValidator.isValid(registrationDto, context));
    }

    @Test
    void isValid_invalidRepeatPassword_NotOk() {
        registrationDto.setPassword(VALID_PASSWORD);
        registrationDto.setRepeatPassword(INVALID_PASSWORD);
        assertFalse(passwordValidator.isValid(registrationDto, context));
    }

    @Test
    void isValid_nullValues_NotOk() {
        assertFalse(passwordValidator.isValid(registrationDto, context));
    }
}
