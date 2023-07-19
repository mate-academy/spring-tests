package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        userRegistrationDto = new UserRegistrationDto();
        context = Mockito.mock(ConstraintValidatorContext.class);
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("12345678");
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto, context));
    }

    @Test
    void isValid_NotEqualsPasswords() {
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("1234567");
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, context));
    }
}
