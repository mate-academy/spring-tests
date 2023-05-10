package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    public void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password passwordAnnotation = Mockito.mock(Password.class);
        Mockito.when(passwordAnnotation.field())
                .thenReturn("password");
        Mockito.when(passwordAnnotation.fieldMatch())
                .thenReturn("repeatPassword");
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(passwordAnnotation);
        userRegistrationDto = testRegistrationDto();
    }

    @Test
    public void isValid_Ok() {
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    public void isValid_notIdenticalPasswords_NotOk() {
        userRegistrationDto.setRepeatPassword("12345677");
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    public void isValid_nullPassword_NotOk() {
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(null);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    private UserRegistrationDto testRegistrationDto() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("larry@gmail.com");
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("12345678");
        return userRegistrationDto;
    }
}
