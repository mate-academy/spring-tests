package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "Abcd1234";
    private static final String INVALID_PASSWORD = "1234567";
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
        ReflectionTestUtils.setField(passwordValidator, "field",
                "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch",
                "repeatPassword");
    }

    @Test
    void isValid_Ok() {
        boolean actual = passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isInValid() {
        userRegistrationDto.setPassword(INVALID_PASSWORD);
        boolean actual = passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}
