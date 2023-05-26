package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {
    private static final String PASSWORD_NAME = "password";
    private static final String REPEAT_PASSWORD_NAME = "repeatPassword";
    private static final String PASSWORD_VALUE_VALID = "12345678";
    private static final String PASSWORD_VALUE_INVALID = "87654321";
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext context;
    private UserRegistrationDto dto;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        Password mockPassword = mock(Password.class);
        when(mockPassword.field()).thenReturn(PASSWORD_NAME);
        when(mockPassword.fieldMatch()).thenReturn(REPEAT_PASSWORD_NAME);
        passwordValidator.initialize(mockPassword);
        dto = new UserRegistrationDto();
        dto.setPassword(PASSWORD_VALUE_VALID);
        dto.setRepeatPassword(PASSWORD_VALUE_VALID);
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        assertTrue(passwordValidator.isValid(dto, context));
    }

    @Test
    void isValid_NotOk() {
        dto.setRepeatPassword(PASSWORD_VALUE_INVALID);
        assertFalse(passwordValidator.isValid(dto, context));
    }
}
