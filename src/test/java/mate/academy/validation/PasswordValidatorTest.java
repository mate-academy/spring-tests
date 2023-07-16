package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "12345678";
    private static final String INVALID_PASSWORD = "1234";

    private final ConstraintValidator<Password, UserRegistrationDto> passwordValidator =
            new PasswordValidator();
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
    private final UserRegistrationDto userRegistrationDto = new UserRegistrationDto();

    @BeforeEach
    void setUp() {
        setField(passwordValidator, "field", "password");
        setField(passwordValidator, "fieldMatch", "repeatPassword");
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
    }

    @Test
    void isValid_validPasswords_ok() {
        assertTrue(passwordValidator.isValid(userRegistrationDto, context));
    }

    @Test
    void isValid_passwordsDoNotMatch_ok() {
        userRegistrationDto.setRepeatPassword(INVALID_PASSWORD);
        assertFalse(passwordValidator.isValid(userRegistrationDto, context));
    }

    @Test
    void isValid_passwordIsNull_ok() {
        userRegistrationDto.setPassword(null);
        assertFalse(passwordValidator.isValid(userRegistrationDto, context));
    }
}
