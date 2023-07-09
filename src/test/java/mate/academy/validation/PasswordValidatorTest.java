package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String TEST_PASSWORD_OK = "12345678";
    private ConstraintValidator<Password, UserRegistrationDto> passwordValidator =
            new PasswordValidator();
    private ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
    private UserRegistrationDto userRegistrationDto = new UserRegistrationDto();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
        userRegistrationDto.setPassword(TEST_PASSWORD_OK);
        userRegistrationDto.setRepeatPassword(TEST_PASSWORD_OK);
    }

    @Test
    void isValid_Ok() {
        boolean actual = passwordValidator.isValid(userRegistrationDto, context);
        assertTrue(actual);
    }

    @Test
    void isValid_Not_Ok() {
        userRegistrationDto.setRepeatPassword("Wrong password");
        boolean actual = passwordValidator.isValid(userRegistrationDto, context);
        assertFalse(actual);
    }
}
