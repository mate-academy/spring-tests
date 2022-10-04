package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private UserRegistrationDto  userRegistrationDto;
    private ConstraintValidatorContext constraintValidatorContext;
    private Password constraintAnnotation;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);

        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("right@mail.com");
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("12345678");

        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_ok() {
        boolean expected = true;

        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);

        assertEquals(expected, actual);
    }

    @Test
    void isValid_passwordNull_notOk() {
        boolean expected = false;

        userRegistrationDto.setPassword(null);
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);

        assertEquals(expected, actual);
    }

    @Test
    void isValid_wrongRepeatPassword_notOk() {
        boolean expected = false;

        userRegistrationDto.setRepeatPassword("12345");
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);

        assertEquals(expected, actual);
    }

    @Test
    void isValid_passwordToShort_notOk() {
        boolean expected = false;

        userRegistrationDto.setPassword("12345");
        userRegistrationDto.setRepeatPassword("12345");
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);

        assertEquals(expected, actual);
    }
}
