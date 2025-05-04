package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String USER_LOGIN = "user@gmail.com";
    private static PasswordValidator passwordValidator;
    @Mock
    private static ConstraintValidatorContext context;

    @BeforeAll
    public static void setUp() {
        passwordValidator = new PasswordValidator();
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
    }

    @Test
    void isValid_validCase_ok() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(USER_LOGIN);
        registrationDto.setPassword("password");
        registrationDto.setRepeatPassword("password");
        Assertions.assertTrue(passwordValidator.isValid(registrationDto, context),
                "Case with a similar passwords should succeed");
    }

    @Test
    void isValid_differentPassword_notOk() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(USER_LOGIN);
        registrationDto.setPassword("password");
        registrationDto.setRepeatPassword("password123");
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context),
                "Case with different passwords should fail");
    }

    @Test
    void isValid_withoutRepeatPassword_notOk() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(USER_LOGIN);
        registrationDto.setPassword("password");
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context),
                "Case without repeat password should fail");
    }
}
