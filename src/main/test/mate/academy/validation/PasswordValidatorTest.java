package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class PasswordValidatorTest {
    private static final String EMAIL = "email@gmail.com";
    private static final String CORRECT_PASSWORD = "qwerty";
    private static final String INCORRECT_PASSWORD = "qwertty";
    @Mock
    private static ConstraintValidatorContext constraintValidatorContext;
    @Mock
    private static Password passwordAnnotation;
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;

    @BeforeAll
    static void beforeAll() {
        constraintValidatorContext = mock(ConstraintValidatorContext.class);
        passwordAnnotation = mock(Password.class);
        when(passwordAnnotation.field()).thenReturn("password");
        when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
    }

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(CORRECT_PASSWORD);
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(passwordAnnotation);
    }

    @Test
    void isValid_correctPassword_ok() {
        userRegistrationDto.setRepeatPassword(CORRECT_PASSWORD);
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_incorrectPassword_notOk() {
        userRegistrationDto.setRepeatPassword(INCORRECT_PASSWORD);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
