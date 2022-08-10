package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static PasswordValidator passwordValidator;
    private static Password constraintAnnotation;
    private static UserRegistrationDto registrationDto;
    private static String email = "denys@gmail.com";
    private static String password = "Qaz2@34dff";
    private String passwordFail = "120";

    @BeforeAll
    static void beforeAll() {
        passwordValidator = new PasswordValidator();
        registrationDto = getRegistrationDto(email, password, password);
        constraintAnnotation = Mockito.mock(Password.class);
    }

    @Test
    void isValid_Password_Ok() {
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
        assertTrue(passwordValidator.isValid(registrationDto, null));
    }

    @Test
    void isValid_notPresentPassword_notOk() {
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
        registrationDto.setRepeatPassword(passwordFail);
        assertFalse(passwordValidator.isValid(registrationDto, null));
    }

    private static UserRegistrationDto getRegistrationDto(String email,
                                                          String password,
                                                          String repeatpassword) {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(email);
        registrationDto.setPassword(password);
        registrationDto.setRepeatPassword(password);
        return registrationDto;
    }
}
