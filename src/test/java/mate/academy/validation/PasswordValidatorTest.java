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

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        userRegistrationDto = new UserRegistrationDto();
        String email = "userBob@gmail.com";
        userRegistrationDto.setEmail(email);
        String password = "qwerty";
        userRegistrationDto.setPassword(password);
        userRegistrationDto.setRepeatPassword(password);
        ReflectionTestUtils.setField(passwordValidator,"field", "password");
        ReflectionTestUtils.setField(passwordValidator,"fieldMatch", "repeatPassword");
    }

    @Test
    void isValid_emptyPassword_notOk() {
        userRegistrationDto.setPassword("");
        assertFalse(passwordValidator.isValid(userRegistrationDto,constraintValidatorContext));
    }

    @Test
    void isValid_nullPassword_notOk() {
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(null);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_wrongPassword_notOk() {
        userRegistrationDto.setRepeatPassword("qwerertertyty");
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
