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
    private ConstraintValidator<Password, UserRegistrationDto> constraintValidator;
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        constraintValidator = new PasswordValidator();
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("bob@i.ua");
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        constraintValidator.initialize(password);
    }

    @Test
    void isValid_nullPassword_notOK() {
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(null);
        assertFalse(constraintValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_notMatchedPassword_notOK() {
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword("123");
        assertFalse(constraintValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_OK() {
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword("1234");
        assertTrue(constraintValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
