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
    private Password passwordAnnotation;
    private UserRegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        passwordAnnotation = Mockito.mock(Password.class);
        Mockito.when(passwordAnnotation.field()).thenReturn("password");
        Mockito.when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
        registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("bob@i.ua");
        registrationDto.setPassword("12345678");
    }

    @Test
    void isValid_passwordsDoNotMatch_notOk() {
        registrationDto.setRepeatPassword("12345");
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_EmptyPasswords_NotOk() {
        registrationDto.setPassword(null);
        registrationDto.setRepeatPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_ok() {
        registrationDto.setRepeatPassword("12345678");
        Assertions.assertTrue(passwordValidator.isValid(registrationDto,
                constraintValidatorContext));
    }
}
