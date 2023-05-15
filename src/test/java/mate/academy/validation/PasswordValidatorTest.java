package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class PasswordValidatorTest {
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext context;
    private static UserRegistrationDto userRegistrationDto;

    @BeforeAll
    static void setupAll() {
        passwordValidator = new PasswordValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
        userRegistrationDto = new UserRegistrationDto();
    }

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
    }

    @Test
    void isValid_ok() {
        userRegistrationDto.setPassword("password123");
        userRegistrationDto.setRepeatPassword("password123");
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto, context),
                "Expected valid passwords");
    }

    @Test
    void isValid_invalidPasswords_notOk() {
        userRegistrationDto.setPassword("password123");
        userRegistrationDto.setRepeatPassword("differentPassword");
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, context),
                "Expected invalid passwords");
    }

    @Test
    void isValid_nullPassword_notOk() {
        userRegistrationDto.setPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, context),
                "Expected invalid passwords");
    }
}
