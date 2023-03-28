package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String PASSWORD = "12345678";
    private static final String CORRECT_REPEAT_PASSWORD = "12345678";
    private static final String INCORRECT_REPEAT_PASSWORD = "qwerty";
    private PasswordValidator passwordValidator;
    private Password annotationConstraint;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        annotationConstraint = Mockito.mock(Password.class);
        Mockito.when(annotationConstraint.field()).thenReturn("password");
        Mockito.when(annotationConstraint.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(annotationConstraint);
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(PASSWORD);
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(CORRECT_REPEAT_PASSWORD);
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_incorrectRepeatPassword_notOk() {
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(INCORRECT_REPEAT_PASSWORD);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
