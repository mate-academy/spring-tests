package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("test@gmail.com");
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("12345678");
        passwordValidator = new PasswordValidator();
    }

    @Test
    void passwordValidator_isValid_Ok() {
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto, null));
    }

    @Test
    void passwordValidator_isValidNotMatch_notOk() {
        userRegistrationDto.setRepeatPassword("1234");
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, null));
    }
}
