package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "12345678";
    private static final String INVALID_PASSWORD = "1111";
    private static ConstraintValidatorContext constraintValidatorContext;
    private static UserRegistrationDto userRegistrationDto;
    private static PasswordValidator passwordValidator;

    @BeforeAll
    static void beforeAll() {
        passwordValidator = new PasswordValidator();
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isInvalid_notOk() {
        userRegistrationDto.setRepeatPassword(INVALID_PASSWORD);
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValidForNull_notOk() {
        userRegistrationDto.setPassword(null);
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }
}
