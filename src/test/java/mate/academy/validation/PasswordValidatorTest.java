package mate.academy.validation;

import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static UserRegistrationDto userDto;

    @BeforeAll
    static void beforeAll() {
        userDto = new UserRegistrationDto();
        constraintValidatorContext = mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
    }

    @Test
    void isValid_Ok() {
        userDto.setPassword("123456789");
        userDto.setRepeatPassword("123456789");
        boolean isValid = passwordValidator.isValid(userDto, constraintValidatorContext);
        Assertions.assertTrue(isValid);
    }

    @Test
    void isValid_NotOk() {
        userDto.setPassword("1234");
        userDto.setRepeatPassword("123456");
        boolean notValid = passwordValidator.isValid(userDto, constraintValidatorContext);
        Assertions.assertFalse(notValid);
    }

    @Test
    void isValid_Null_NotOk() {
        userDto.setPassword(null);
        userDto.setRepeatPassword(null);
        boolean notValid = passwordValidator.isValid(userDto, constraintValidatorContext);
        Assertions.assertFalse(notValid);
    }
}
