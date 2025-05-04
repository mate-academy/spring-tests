package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "password";
    private static final String INVALID_PASSWORD = "repeatPassword";
    private UserRegistrationDto userRegistrationDto;
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
    }

    @Test
    void isValid_Ok() {
        boolean actual = passwordValidator.isValid(userRegistrationDto, context);
        Assertions.assertTrue(actual,
                String.format("Result should be true for password: %s, and repeat password: "
                        + "%s, but was false", VALID_PASSWORD, VALID_PASSWORD));
    }

    @Test
    void isValid_passwordIsNotEquals_Ok() {
        userRegistrationDto.setRepeatPassword(INVALID_PASSWORD);
        boolean actual = passwordValidator.isValid(userRegistrationDto, context);
        Assertions.assertFalse(actual,
                String.format("Result should be false for password: %s, and repeat password: "
                        + "%s, but was true", VALID_PASSWORD, INVALID_PASSWORD));
    }

    @Test
    void isValid_passwordIsNull_Ok() {
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
        boolean actual = passwordValidator.isValid(userRegistrationDto, context);
        Assertions.assertFalse(actual,
                "Result should be false for null password but was true");
    }
}
