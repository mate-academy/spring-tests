package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private Password password;

    @BeforeEach
    public void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        password = Mockito.mock(Password.class);
        passwordValidator = new PasswordValidator();
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
    }

    @Test
    public void isValid_ok() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("bob@i.ua");
        dto.setPassword("1234");
        dto.setRepeatPassword("1234");
        Assertions.assertTrue(passwordValidator.isValid(dto, constraintValidatorContext));
    }

    @Test
    public void isValid_passwordsDoNotMatch_ok() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("bob@i.ua");
        dto.setPassword("1234");
        dto.setRepeatPassword("123");
        Assertions.assertFalse(passwordValidator.isValid(dto, constraintValidatorContext));
    }

    @Test
    public void isValid_nullPassword_ok() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("bob@i.ua");
        dto.setPassword(null);
        dto.setRepeatPassword("1234");
        Assertions.assertFalse(passwordValidator.isValid(dto, constraintValidatorContext));
    }

    @Test
    public void isValid_nullRepeatPassword_ok() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("bob@i.ua");
        dto.setPassword("1234");
        dto.setRepeatPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(dto, constraintValidatorContext));
    }
}
