package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PasswordValidatorTest {
    private static final String EMAIL = "test123@testmail.net";
    private static final String PASSWORD = "12345678";
    private static final String WRONG_PASSWORD = "123456789";
    private static UserRegistrationDto userRegistrationDto;
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static Password password;

    @BeforeAll
    static void beforeAll() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
    }

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(PASSWORD);
    }

    @Test
    void validPassword_ok() {
        boolean expected =
                passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertTrue(expected);
    }

    @Test
    void invalidPassword_notOk() {
        userRegistrationDto.setRepeatPassword(WRONG_PASSWORD);
        boolean expected =
                passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(expected);
    }
}
