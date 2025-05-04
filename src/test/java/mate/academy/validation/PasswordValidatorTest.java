package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {
    private static final String PASSWORD = "1234";
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;
    private final PasswordValidator passwordValidator = new PasswordValidator();
    @Mock
    private Password constraintAnnotation;

    @BeforeEach
    void setUp() {
        userRegistrationDto = createUserRegistrationDto();
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_Ok() {
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_NotMatchedPasswords_NotOk() {
        userRegistrationDto.setRepeatPassword(PASSWORD + ".");
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_NullPassword_NotOk() {
        userRegistrationDto.setPassword(null);
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    @Test
    void isValid_NullRepeatPassword_NotOk() {
        userRegistrationDto.setRepeatPassword(null);
        boolean actual = passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }

    private UserRegistrationDto createUserRegistrationDto() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setPassword(PASSWORD);
        dto.setRepeatPassword(PASSWORD);
        return dto;
    }
}
