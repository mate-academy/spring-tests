package mate.academy.validation;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private Password constraintAnnotation;

    @BeforeEach
    void setUp() {
        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");

        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_Ok() {
        String password = "1234";
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(password);
        userRegistrationDto.setRepeatPassword(password);
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto, null),
                "The same passwords");
    }

    @Test
    void isValid_NotOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, null));
        String password = "12345";
        userRegistrationDto.setPassword(password);
        userRegistrationDto.setRepeatPassword(password + "appendix");
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto, null),
                "Different passwords");
    }
}
