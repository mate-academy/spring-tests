package mate.academy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "valid_password";
    private static final String INVALID_PASSWORD = "invalid_password";
    private ConstraintValidator constraintValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        constraintValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        ReflectionTestUtils.setField(constraintValidator, "field", "password");
        ReflectionTestUtils.setField(constraintValidator, "fieldMatch", "repeatPassword");
        userRegistrationDto = new UserRegistrationDto();
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
        boolean isValid = constraintValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertTrue(isValid);
    }

    @Test
    void isValid_invalidPassword_notOk() {
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(INVALID_PASSWORD);
        boolean isValid = constraintValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }
}
