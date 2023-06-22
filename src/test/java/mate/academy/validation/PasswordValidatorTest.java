package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {
    private static final String PASSWORD = "12345";
    private static final String PASSWORD_TAG = "password";
    private static final String REPEAT_PASSWORD_TAG = "repeatPassword";
    private static final String ANOTHER_PASSWORD = "another";
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;
    @Mock
    private Password constraintAnnotation;
    @Mock
    private UserRegistrationDto userRegistrationDto;
    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        ReflectionTestUtils.setField(passwordValidator, "field", PASSWORD_TAG);
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", REPEAT_PASSWORD_TAG);
        Mockito.when(constraintAnnotation.field()).thenReturn(PASSWORD_TAG);
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn(REPEAT_PASSWORD_TAG);
        passwordValidator.initialize(constraintAnnotation);
        Mockito.when(new BeanWrapperImpl(userRegistrationDto)
                .getPropertyValue(PASSWORD_TAG)).thenReturn(PASSWORD);
    }

    @Test
    void isValid_similarPasswords_ok() {
        Mockito.when(new BeanWrapperImpl(userRegistrationDto)
                .getPropertyValue(REPEAT_PASSWORD_TAG)).thenReturn(PASSWORD);
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext),
                "method should return true if password and repeatPassword is the same");
    }

    @Test
    void isValid_differentPasswords_notOk() {
        Mockito.when(new BeanWrapperImpl(userRegistrationDto)
                .getPropertyValue(REPEAT_PASSWORD_TAG)).thenReturn(ANOTHER_PASSWORD);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext),
                "method should return false if password and repeatPassword is different");
    }

    @Test
    void isValid_nullPasswords_notOk() {
        Mockito.when(new BeanWrapperImpl(userRegistrationDto)
                .getPropertyValue(REPEAT_PASSWORD_TAG)).thenReturn(null);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext),
                "method should return false if password or repeatPassword is null");
        Mockito.when(new BeanWrapperImpl(userRegistrationDto)
                .getPropertyValue(PASSWORD_TAG)).thenReturn(null);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext),
                "method should return false if password and repeatPassword is null");
    }
}
