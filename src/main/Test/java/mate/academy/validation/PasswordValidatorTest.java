package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.validation.ConstraintValidatorContext;

class PasswordValidatorTest {
    private final static String EMAIL = "bchupika@mate.academy";
    private final static String PASSWORD = "1234";
    private ConstraintValidatorContext constraintValidatorContext;
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(PASSWORD);
        Password constraintAnnotation;
        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_wrongRepeatPassword_notOk() {
        userRegistrationDto.setRepeatPassword("4321");
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }
}
