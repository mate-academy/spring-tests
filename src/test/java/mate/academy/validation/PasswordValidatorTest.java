package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {
    private static PasswordValidator passwordValidator;
    @Mock
    private static ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        passwordValidator = new PasswordValidator();
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
    }

    @Test
    void isValid_passwordEqualsRepeatPassword_ok() {
        UserRegistrationDto dto = new UserRegistrationDto();
        String password = "password";
        dto.setPassword(password);
        dto.setRepeatPassword(password);
        boolean actual = passwordValidator.isValid(dto, context);
        Assertions.assertTrue(actual,
                "It should return true if password equals repeat password\n");
    }

    @Test
    void isValid_PasswordDoNotMatchRepeatPassword_ok() {
        UserRegistrationDto dto = new UserRegistrationDto();
        String password = "password";
        dto.setPassword(password);
        dto.setRepeatPassword("something else");
        boolean actual = passwordValidator.isValid(dto, context);
        Assertions.assertFalse(actual,
                "It should return false if password do not match repeat password\n");
    }

    @Test
    void isValid_passwordIsNull_ok() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setPassword(null);
        boolean actual = passwordValidator.isValid(dto, context);
        Assertions.assertFalse(actual,
                "It should return false if password is null\n");
    }
}
