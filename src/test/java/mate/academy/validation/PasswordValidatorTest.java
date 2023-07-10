package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private ConstraintValidatorContext context;
    private PasswordValidator passwordValidator;

    @BeforeEach
    public void setUp() {
        passwordValidator = new PasswordValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
        Password passwordAnnotation = Mockito.mock(Password.class);
        Mockito.when(passwordAnnotation.field()).thenReturn("password");
        Mockito.when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
    }

    @Test
    public void isValid_ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword("1234");
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto, context));
    }

    @Test
    public void isValid_ArentMatch_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword("12345");
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, context));
    }

    @Test
    public void isValid_emptyFields_ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("");
        userRegistrationDto.setRepeatPassword("");
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto, context));
    }

    @Test
    public void isValid_nullPassword_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword("1234");
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, context));
    }

    @Test
    public void isValid_nullRepeatPassword_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, context));
    }
}
