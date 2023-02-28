package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "123456789";
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password mock = Mockito.mock(Password.class);
        Mockito.when(mock.field()).thenReturn("password");
        Mockito.when(mock.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(mock);
    }

    @Test
    void isValid_Ok() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(PASSWORD);
        registrationDto.setEmail(EMAIL);
        Assertions.assertTrue(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_DifferentPasswords() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword("987654321");
        registrationDto.setEmail(EMAIL);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }
}
