package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String PASSWORD = "123456789";
    private UserRegistrationDto userDto;
    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        Password constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
        userDto = new UserRegistrationDto();
    }

    @Test
    void isValid_ok() {
        userDto.setPassword(PASSWORD);
        userDto.setRepeatPassword(PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(userDto, null),
                "Should be true for same passwords");
    }

    @Test
    void isValid_notOk() {
        userDto.setPassword(PASSWORD);
        userDto.setRepeatPassword("notPassword");
        Assertions.assertFalse(passwordValidator.isValid(userDto, null),
                "Should be false for different passwords");
    }
}
