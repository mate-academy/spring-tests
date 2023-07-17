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
    private ConstraintValidatorContext context;
    private PasswordValidator passwordValidator;

    @BeforeEach
    public void setUp() {
        passwordValidator = new PasswordValidator();
        context = mock(ConstraintValidatorContext.class);
        Password passwordAnnotation = mock(Password.class);
        when(passwordAnnotation.field()).thenReturn("password");
        when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
    }

    @Test
    public void isValid_ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword("1234");
        assertTrue(passwordValidator.isValid(userRegistrationDto, context));
    }

    @Test
    public void isValid_ArentMatch_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword("12345");
        assertFalse(passwordValidator.isValid(userRegistrationDto, context));
    }

    @Test
    public void isValid_emptyFields_ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("");
        userRegistrationDto.setRepeatPassword("");
        assertTrue(passwordValidator.isValid(userRegistrationDto, context));
    }

    @Test
    public void isValid_nullPassword_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword("1234");
        assertFalse(passwordValidator.isValid(userRegistrationDto, context));
    }

    @Test
    public void isValid_nullRepeatPassword_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword(null);
        assertFalse(passwordValidator.isValid(userRegistrationDto, context));
    }
}
