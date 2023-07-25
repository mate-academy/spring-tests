package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {

    @Mock
    private Password passwordAnnotation;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private PasswordValidator passwordValidator;
    private String validPassword;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        validPassword = "validPassword";
    }

    @Test
    void isValid_equalPasswords_ok() {
        when(passwordAnnotation.field()).thenReturn("password");
        when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(validPassword);
        userRegistrationDto.setRepeatPassword(validPassword);
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_unequalPasswords_ok() {
        when(passwordAnnotation.field()).thenReturn("password");
        when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(validPassword);
        String invalidPassword = "invalidPassword";
        userRegistrationDto.setRepeatPassword(invalidPassword);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_unequalPasswordsEmpty_ok() {
        when(passwordAnnotation.field()).thenReturn("password");
        when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(validPassword);
        String emptyPassword = "";
        userRegistrationDto.setRepeatPassword(emptyPassword);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_differentCase_ok() {
        when(passwordAnnotation.field()).thenReturn("password");
        when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(validPassword);
        String upperCasePassword = "VALIDPASSWORD";
        userRegistrationDto.setRepeatPassword(upperCasePassword);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_spaceOnZeroIndex_ok() {
        when(passwordAnnotation.field()).thenReturn("password");
        when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        String spaceOnZeroIndexPassword = " validPassword";
        userRegistrationDto.setPassword(validPassword);
        userRegistrationDto.setRepeatPassword(spaceOnZeroIndexPassword);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_spaceOnLastIndex_ok() {
        when(passwordAnnotation.field()).thenReturn("password");
        when(passwordAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(passwordAnnotation);
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(validPassword);
        String spaceOnLastIndexPassword = "validPassword ";
        userRegistrationDto.setRepeatPassword(spaceOnLastIndexPassword);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
