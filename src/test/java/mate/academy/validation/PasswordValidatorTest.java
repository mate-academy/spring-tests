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
    private static final String PASSWORD = "12345678";
    private static final String INVALID_REPEAT_PASSWORD = "12345677";
    private UserRegistrationDto userRegistrationDto;
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = mock(ConstraintValidatorContext.class);
        Password password = mock(Password.class);
        when(password.field()).thenReturn("password");
        when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
    }

    @Test
    void isValid_ok() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(PASSWORD);
        assertTrue(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(INVALID_REPEAT_PASSWORD);
        assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }
}
