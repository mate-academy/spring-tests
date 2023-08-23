package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class PasswordValidatorTest {
    private static final String PASSWORD = "12345678";
    private static final String EMAIL = "bob@gmail.com";
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private Password passwordAnnotation;
    private UserRegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordValidator = new PasswordValidator();
        passwordAnnotation = Mockito.mock(Password.class);
        Mockito.when(passwordAnnotation.field()).thenReturn("password");
        Mockito.when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
        registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(EMAIL);
        registrationDto.setPassword(PASSWORD);
    }

    @Test
    void isValid_matchingPasswords_returnsTrue() {
        registrationDto.setRepeatPassword(PASSWORD);
        assertTrue(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_nonMatchingPasswords_returnsFalse() {
        registrationDto.setRepeatPassword("different");
        assertFalse(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_nullPasswords_returnsFalse() {
        registrationDto.setPassword(null);
        registrationDto.setRepeatPassword(null);
        assertFalse(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }
}
