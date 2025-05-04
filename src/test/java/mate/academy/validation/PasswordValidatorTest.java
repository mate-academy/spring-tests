package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext context;

    @BeforeAll
    static void beforeAll() {
        context = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
    }

    @Test
    void isValid_validPasswords_ok() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("denik@gmail.com");
        registrationDto.setPassword("password");
        registrationDto.setRepeatPassword("password");
        Assertions.assertTrue(passwordValidator.isValid(registrationDto, context));
    }

    @Test
    void isValid_differentPasswords_notOk() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("denik@gmail.com");
        registrationDto.setPassword("password");
        registrationDto.setRepeatPassword("drowssap");
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context));
    }

    @Test
    void isValid_nullPasswords_notOk() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("denik@gmail.com");
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context));
    }
}
