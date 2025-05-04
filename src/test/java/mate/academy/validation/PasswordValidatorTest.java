package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static UserRegistrationDto userRegistrationDto;
    private static String email = "johnn@ukr.net";
    private static String password = "12345678";

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(email);
        userRegistrationDto.setPassword(password);
        userRegistrationDto.setRepeatPassword(password);
        ReflectionTestUtils.setField(passwordValidator,"field", "password");
        ReflectionTestUtils.setField(passwordValidator,"fieldMatch", "repeatPassword");
    }

    @Test
    void isValid_emptyPassword_NotOk() {
        userRegistrationDto.setPassword("");
        assertFalse(passwordValidator.isValid(userRegistrationDto,constraintValidatorContext));
    }

    @Test
    void isValid_Null_NotOk() {
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(null);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_differentPasswords_NotOk() {
        userRegistrationDto.setRepeatPassword("87654321");
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
