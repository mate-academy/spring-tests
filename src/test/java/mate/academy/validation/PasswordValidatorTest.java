package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String PASSWORD = "1234";
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        registrationDto = new UserRegistrationDto();
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password annotation = Mockito.mock(Password.class);
        Mockito.when(annotation.field()).thenReturn("password");
        Mockito.when(annotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(annotation);
    }

    @Test
    void isValid_Ok() {
        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_RepeatPassword_NotOk() {
        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(PASSWORD + "zzz");
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));
    }
}
