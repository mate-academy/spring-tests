package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "12345678";
    private UserRegistrationDto userRegistrationDto;
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(VALID_PASSWORD);
        userRegistrationDto.setRepeatPassword(VALID_PASSWORD);
    }

    @Test
    void isValid_Ok() {
        Assertions.assertTrue(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_InvalidPassword_Ok() {
        userRegistrationDto.setPassword("123");
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }
}
