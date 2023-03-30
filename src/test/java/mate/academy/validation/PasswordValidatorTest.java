package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "1234";
    private static final String INVALID_PASSWORD = "4321";
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
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto,
                        constraintValidatorContext),
                "Method should return true for the same passwords");
    }

    @Test
    void isValid_repeatPasswordIsNotEquals_notOk() {
        userRegistrationDto.setRepeatPassword(INVALID_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                        constraintValidatorContext),
                "Method should return false for the invalid repeat password");
    }
}
