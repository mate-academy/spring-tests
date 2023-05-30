package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String PASSWORD = "12345";
    private static final String WRONG_REPEAT_PASSWORD = "7777";
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(PASSWORD);

        passwordValidator = new PasswordValidator();

        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        Assertions.assertTrue(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_wrongRepeatPassword_notOk() {
        userRegistrationDto.setRepeatPassword(WRONG_REPEAT_PASSWORD);
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_passwordIsNull_notOk() {
        userRegistrationDto.setPassword(null);
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }
}
