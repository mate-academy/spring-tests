package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PasswordValidatorTest {
    private static ConstraintValidatorContext constraintValidatorContext;
    private PasswordValidator passwordValidator;
    private static Password passwordAnnotation;
    private UserRegistrationDto userRegistrationDto;
    private static final String EMAIL = "bchupika@mate.academy";
    private static final String CORRECT_PASSWORD = "password";
    private static final String INCORRECT_PASSWORD = "password123";

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
    void isValid_correctData_ok() {
        userRegistrationDto.setRepeatPassword(CORRECT_PASSWORD);
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_incorrectData_notOk() {
        userRegistrationDto.setRepeatPassword(INCORRECT_PASSWORD);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}