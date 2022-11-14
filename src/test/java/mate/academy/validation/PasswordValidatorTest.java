package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String USER_PASSWORD = "password";
    private static final String USER_WRONG_PASSWORD = "wrong";
    private ConstraintValidatorContext constraintValidator;
    private PasswordValidator passwordValidator;
    private UserRegistrationDto registrationDto;
    private Password constraintAnnotations;

    @BeforeEach
    void setUp() {
        constraintValidator = Mockito.spy(ConstraintValidatorContext.class);
        passwordValidator = Mockito.spy(PasswordValidator.class);
        constraintAnnotations = Mockito.spy(Password.class);
    }

    @Test
    void isValid() {
        ReflectionTestUtils.setField(passwordValidator, "field", USER_PASSWORD);
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", USER_PASSWORD);
        registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(USER_EMAIL);
        registrationDto.setPassword(USER_PASSWORD);
        registrationDto.setRepeatPassword(USER_PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(registrationDto, constraintValidator));
    }

    @Test
    void isValid_WrongPassword_NotOk() {
        ReflectionTestUtils.setField(passwordValidator, "field", USER_PASSWORD);
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
        registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(USER_EMAIL);
        registrationDto.setPassword(USER_PASSWORD);
        registrationDto.setRepeatPassword(USER_WRONG_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, constraintValidator));
    }
}
