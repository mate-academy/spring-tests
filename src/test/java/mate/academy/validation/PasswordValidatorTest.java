package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private ConstraintValidator<Password, UserRegistrationDto> validator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        validator = new PasswordValidator();
    }

    @Test
    void isValid_ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("harrington@.ua");
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword("1234");
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        validator.initialize(password);
        assertTrue(validator.isValid(userRegistrationDto,constraintValidatorContext));
    }

    @Test
    void isValid_wrongRepeatPassword_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("harrington@.ua");
        userRegistrationDto.setPassword("1233");
        userRegistrationDto.setRepeatPassword("1234");
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        validator.initialize(password);
        assertFalse(validator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_nullPassword_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("harrington@.ua");
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(null);
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        validator.initialize(password);
        assertFalse(validator.isValid(userRegistrationDto, constraintValidatorContext));
        assertFalse(validator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
