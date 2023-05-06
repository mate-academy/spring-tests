package mate.academy.validation;

import java.lang.reflect.Field;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        passwordValidator = new PasswordValidator();
        Field field = passwordValidator.getClass().getDeclaredField("field");
        Field fieldMatch = passwordValidator.getClass().getDeclaredField("fieldMatch");
        field.setAccessible(true);
        field.set(passwordValidator, "password");
        fieldMatch.setAccessible(true);
        fieldMatch.set(passwordValidator, "repeatPassword");
    }

    @Test
    void isValid_validValue_ok() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("bob@gmail.com");
        registrationDto.setPassword("12345678");
        registrationDto.setRepeatPassword("12345678");

        boolean actual = passwordValidator.isValid(registrationDto, context);
        Assertions.assertTrue(actual, "Method should return true for password "
                + registrationDto.getPassword());
    }

    @Test
    void isValid_nullValue_notOk() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("bob@gmail.com");
        registrationDto.setPassword(null);
        registrationDto.setRepeatPassword("12345678");

        boolean actual = passwordValidator.isValid(registrationDto, context);
        Assertions.assertFalse(actual, "Method should return false for null value password");
    }

    @Test
    void isValid_passwordsDontMatch_notOk() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("bob@gmail.com");
        registrationDto.setPassword("12345677");
        registrationDto.setRepeatPassword("12345678");

        boolean actual = passwordValidator.isValid(registrationDto, context);
        Assertions.assertFalse(actual,
                "Method should return false when password and repeatPassword don't match");
    }
}
