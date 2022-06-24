package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import javax.validation.ConstraintValidatorContext;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private String password;
    private String correctRepeatPassword;
    private String incorrectRepeatPassword;
    private String email;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        password = "12345678";
        correctRepeatPassword = "12345678";
        incorrectRepeatPassword = "12231";
        email = "bchupika@mate.academy";
        ReflectionTestUtils.setField(passwordValidator,"field","password");
        ReflectionTestUtils.setField(passwordValidator,"fieldMatch","repeatPassword");
    }

    @Test
    void isValid_Ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(email);
        userRegistrationDto.setPassword(password);
        userRegistrationDto.setRepeatPassword(correctRepeatPassword);
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertTrue(actual,"true is expected for " + userRegistrationDto);
    }

    @Test
    void isValid_NotOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(email);
        userRegistrationDto.setPassword(password);
        userRegistrationDto.setRepeatPassword(incorrectRepeatPassword);
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual,"false is expected for " + userRegistrationDto);
    }
}