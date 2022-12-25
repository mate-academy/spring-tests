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
    private UserRegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password annotation = Mockito.mock(Password.class);
        Mockito.when(annotation.field()).thenReturn("password");
        Mockito.when(annotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(annotation);
        registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("bob123@gmail.com");
        registrationDto.setPassword("bob123");
        registrationDto.setRepeatPassword("bob123");
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(passwordValidator
                .isValid(registrationDto, constraintValidatorContext));
    }

    @Test
    void isValidInvalidPassword_NotOk() {
        registrationDto.setRepeatPassword("bob");
        Assertions.assertFalse(passwordValidator
                .isValid(registrationDto, constraintValidatorContext));
    }
}
