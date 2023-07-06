package mate.academy.validation;

import java.lang.reflect.Field;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private UserRegistrationDto bobRegistrationDto;
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        bobRegistrationDto = new UserRegistrationDto();
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        bobRegistrationDto.setEmail("bob@gmail.com");
        Field field = passwordValidator.getClass().getDeclaredField("field");
        field.setAccessible(true);
        field.set(passwordValidator, "password");
        Field fieldMatch = passwordValidator.getClass().getDeclaredField("fieldMatch");
        fieldMatch.setAccessible(true);
        fieldMatch.set(passwordValidator, "repeatPassword");
    }

    @Test
    void isValid_Ok() {
        bobRegistrationDto.setPassword("12345678");
        bobRegistrationDto.setRepeatPassword("12345678");
        boolean actual = passwordValidator.isValid(bobRegistrationDto, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_passwordsIsNull_NotOk() {
        boolean actual = passwordValidator.isValid(bobRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_passwordsDontMatch_NotOk() {
        bobRegistrationDto.setPassword("12345678");
        bobRegistrationDto.setRepeatPassword("123456789");
        boolean actual = passwordValidator.isValid(bobRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}
