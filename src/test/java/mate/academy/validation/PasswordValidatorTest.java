package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    public static final String EMAIL = "bchupika@mate.academy";
    public static final String PASSWORD = "12345678";
    public static final String INVALID_PASSWORD = "123554";
    public static final String BLANK_PASSWORD = "";
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext context;
    private Password password;
    private UserRegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        password = Mockito.mock(Password.class);
        context = Mockito.mock(ConstraintValidatorContext.class);

        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);

        registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(EMAIL);
        registrationDto.setPassword(PASSWORD);
        registrationDto.setRepeatPassword(PASSWORD);
    }

    @Test
    void isValid_OK() {
        Assertions.assertTrue(passwordValidator.isValid(registrationDto, context));
    }

    @Test
    void invalidPassword_NotOk() {
        registrationDto.setRepeatPassword(INVALID_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context));
    }

    @Test
    void blankPassword_NotOk() {
        registrationDto.setRepeatPassword(BLANK_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context));
    }

    @Test
    void nullPassword_NotOk() {
        registrationDto.setRepeatPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(registrationDto, context));
    }
}
