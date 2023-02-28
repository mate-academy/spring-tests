package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;
    @Mock
    private Password constraintAnnotation;

    @BeforeEach
    void setUp() {
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("qwerty");
        userRegistrationDto.setRepeatPassword("qwerty");
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        userRegistrationDto.setPassword("qwerty1");
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_nullPassword_notOk() {
        userRegistrationDto.setPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_nullRepeatPassword_notOk() {
        userRegistrationDto.setRepeatPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }
}
