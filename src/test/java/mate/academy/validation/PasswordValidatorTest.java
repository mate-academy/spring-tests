package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import javax.validation.ConstraintValidatorContext;

class PasswordValidatorTest {
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static UserRegistrationDto userRegistrationDto;

    @BeforeAll
    static void beforeAll() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        ReflectionTestUtils.setField(passwordValidator,"field","password");
        ReflectionTestUtils.setField(passwordValidator,"fieldMatch","repeatPassword");
        String correctRepeatPassword = "12345678";
        String email = "bchupika@mate.academy";
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(email);
        userRegistrationDto.setRepeatPassword(correctRepeatPassword);
    }

    @Test
    void isValid_Ok() {
        String correctPassword = "12345678";
        userRegistrationDto.setPassword(correctPassword);
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertTrue(actual,"true is expected for " + userRegistrationDto);
    }

    @Test
    void isValid_invalidPassword_NotOk() {
        String incorrectPassword = "1111";
        userRegistrationDto.setPassword(incorrectPassword);
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual,"false is expected for " + userRegistrationDto);
    }
}
