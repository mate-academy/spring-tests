package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext context;
    private UserRegistrationDto userRegistrationDto;
    private Password password;

    @BeforeEach
    void setup() {
        passwordValidator = new PasswordValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword("password123");
        userRegistrationDto.setRepeatPassword("password123");
        password = Mockito.mock(Password.class);
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto, context),
                "Expected valid passwords");
    }

    @Test
    void isValid_invalidPasswords_notOk() {
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
