package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "qwert";
    private static final String WRONG_PASSWORD = "wrong";
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
    void passwordValid_ok() {
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertTrue(actual, String.format("Result should be true for password: %s,"
                + " and repeat password: "
                + "%s, but was false", VALID_PASSWORD, VALID_PASSWORD));
    }

    @Test
    void wrongPassword_notOk() {
        userRegistrationDto.setRepeatPassword(WRONG_PASSWORD);
        boolean actual = passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertTrue(actual, String.format("Should be false for password: %s,"
                + " and repeat password: "
                + "%s, but was true", VALID_PASSWORD, WRONG_PASSWORD));
    }

    @Test
    void passwordIsNull_NotOk() {
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual,
                "Result can't be true");
    }
}
