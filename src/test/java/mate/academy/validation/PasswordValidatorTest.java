package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String USER_EMAIL = "user@i.ua";
    private static final String USER_PASSWORD = "qwerty";
    private static final String USER_REPEATED_PASSWORD = "qwerty";
    private static final String USER_WRONG_REPEATED_PASSWORD = "ytrewq";
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void beforeAll() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        ReflectionTestUtils.setField(passwordValidator,"field","password");
        ReflectionTestUtils.setField(passwordValidator,"fieldMatch","repeatPassword");
    }

    @Test
    void isValid_userPasswordsMatch_Ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(USER_EMAIL);
        userRegistrationDto.setPassword(USER_PASSWORD);
        userRegistrationDto.setRepeatPassword(USER_REPEATED_PASSWORD);
        Assertions.assertTrue(passwordValidator
                .isValid(userRegistrationDto,constraintValidatorContext));
    }

    @Test
    void isValid_userPasswordsNotMatch_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(USER_EMAIL);
        userRegistrationDto.setPassword(USER_PASSWORD);
        userRegistrationDto.setRepeatPassword(USER_WRONG_REPEATED_PASSWORD);
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto,constraintValidatorContext));
    }

    @Test
    void isValid_userPasswordsNull_notOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(USER_EMAIL);
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(null);
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto,constraintValidatorContext));
    }
}
