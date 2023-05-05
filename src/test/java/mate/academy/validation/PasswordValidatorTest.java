package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "1234";
    private static final String INVALID_PASSWORD = "12345";
    private UserRegistrationDto userRegistrationDto;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;
    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
        passwordValidator = new PasswordValidator();
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_passwordNotEqualsRepeatPassword_NotOk() {
        userRegistrationDto.setRepeatPassword(INVALID_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_passwordOrRepeatPasswordIsNull_NotOk() {
        userRegistrationDto.setRepeatPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }
}
