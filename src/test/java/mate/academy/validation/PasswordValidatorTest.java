package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "1234";
    private static final String INVALID_PASSWORD = "invalid";
    private UserRegistrationDto userRegistrationDto;
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

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
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertTrue(actual,
                String.format("Should be true for password: %s, and repeat password: "
                        + "%s, but was false", VALID_PASSWORD, VALID_PASSWORD));
    }

    @Test
    void isValid_NotOk() {
        userRegistrationDto.setRepeatPassword(INVALID_PASSWORD);
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual,
                String.format("Should be false for password: %s, and repeat password: "
                        + "%s, but was true", VALID_PASSWORD, INVALID_PASSWORD));
    }
}
