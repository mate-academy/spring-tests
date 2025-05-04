package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private UserRegistrationDto registrationDto;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        registrationDto = new UserRegistrationDto();
        setField(passwordValidator, "field", "password");
        setField(passwordValidator, "fieldMatch", "repeatPassword");
    }

    @Test
    void isValid_matchedRequest_ok() {
        registrationDto.setPassword("1234");
        registrationDto.setRepeatPassword("1234");
        boolean actual = passwordValidator.isValid(registrationDto, constraintValidatorContext);
        assertTrue(actual,
                "Method should return true if password matches repeat password");
    }

    @Test
    void isValid_notMatchedRequest_notOk() {
        registrationDto.setPassword("8765");
        registrationDto.setRepeatPassword("1234");
        boolean actual = passwordValidator.isValid(registrationDto, constraintValidatorContext);
        assertFalse(actual,
                "Method should return false if password do not matches repeat password");
    }

    @Test
    void isValid_passwordNull_notOk() {
        registrationDto.setPassword(null);
        registrationDto.setRepeatPassword(null);
        boolean actual = passwordValidator.isValid(registrationDto, constraintValidatorContext);
        assertFalse(actual,
                "Method should return false if password is null");
    }
}
