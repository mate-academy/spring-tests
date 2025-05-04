package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "12345678";
    private static final String INVALID_PASSWORD = "12";
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static UserRegistrationDto userRegistrationDto;
    private static Password annotation;

    @BeforeAll
    static void beforeAll() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        annotation = Mockito.mock(Password.class);
        when(annotation.field()).thenReturn("password");
        when(annotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(annotation);
    }

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(PASSWORD);
    }

    @Test
    void isValid_ok() {
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        assertTrue(actual);
    }

    @Test
    void isValid_wrongRepeatPassword_notOk() {
        userRegistrationDto.setRepeatPassword(INVALID_PASSWORD);
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        assertFalse(actual);
    }
}
