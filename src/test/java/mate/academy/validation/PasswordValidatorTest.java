package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private Password constraintAnnotation;
    private UserRegistrationDto registrationDto;
    private String email = "denys@gmail.com";
    private String password = "Qaz2@34dff";
    private String passwordFail = "120";

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        registrationDto = getRegistrationDto(email, password, password);
        constraintAnnotation = Mockito.mock(Password.class);
    }

    @Test
    void isValid_Ok() {
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
        assertTrue(passwordValidator.isValid(registrationDto, null));
    }

    @Test
    void isValid_notOk() {
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
        registrationDto.setRepeatPassword(passwordFail);
        assertFalse(passwordValidator.isValid(registrationDto, null));
    }

    private UserRegistrationDto getRegistrationDto(String email,
                                                   String password, String repeatpassword) {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(email);
        registrationDto.setPassword(password);
        registrationDto.setRepeatPassword(password);
        return registrationDto;
    }
}
