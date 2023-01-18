package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {
    private static final String EMAIL = "user@gmail.com";
    private static final String PASSWORD = "12345";
    private static final String REPEAT_PASSWORD = "12345";
    private static final String WRONG_REPEAT_PASSWORD = "12346";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_REPEAT_PASSWORD = "repeatPassword";
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext constraintValidatorContext;
    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = mock(ConstraintValidatorContext.class);
        Password constraintAnnotation = mock(Password.class);
        when(constraintAnnotation.field())
                .thenReturn(FIELD_PASSWORD);
        when(constraintAnnotation.fieldMatch())
                .thenReturn(FIELD_REPEAT_PASSWORD);
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
        userRegistrationDto = getUserRegistrationDto();
    }

    @Test
    void isValid_Ok() {
        assertTrue(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_NullPassword_NotOk() {
        userRegistrationDto.setPassword(null);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_WrongRepeatPassword_NotOk() {
        userRegistrationDto.setRepeatPassword(WRONG_REPEAT_PASSWORD);
        assertFalse(passwordValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    private UserRegistrationDto getUserRegistrationDto() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(REPEAT_PASSWORD);
        return userRegistrationDto;
    }
}
