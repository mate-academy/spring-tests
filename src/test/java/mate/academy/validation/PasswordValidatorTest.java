package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String VALID_PASS = "12345678";
    private static final String INVALID_PASS = "1";

    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(VALID_PASS);
        userRegistrationDto.setRepeatPassword(VALID_PASS);
    }

    @Test
    void isValid_Pass_ok() {
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_WrongPassword_notOk() {
        userRegistrationDto.setPassword(INVALID_PASS);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
