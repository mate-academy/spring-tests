package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
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
        registrationDto.setEmail("user@gmail.com");
        registrationDto.setPassword("password");
        registrationDto.setRepeatPassword("password");
        Assertions.assertTrue(passwordValidator.isValid(registrationDto, context),
                "Case with a similar passwords should succeed");
    }

    @Test
    void is_Valid_differentPassword_notOk() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("user@gmail.com");
        registrationDto.setPassword("password");
        registrationDto.setRepeatPassword("password123");
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context),
                "Case with different passwords should fail");
    }

    @Test
    void is_Valid_withoutRepeatPassword_notOk() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("user@gmail.com");
        registrationDto.setPassword("password");
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context),
                "Case without repeat password should fail");
    }
}
