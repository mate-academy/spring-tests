package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String PASSWORDFAIL = "120";
    private static PasswordValidator passwordValidator;
    private static Password constraintAnnotation;
    private UserRegistrationDto registrationDto;

    @BeforeAll
    static void beforeAll() {
        constraintAnnotation = Mockito.mock(Password.class);
        passwordValidator = new PasswordValidator();
    }

    @BeforeEach
    void setUp() {
        registrationDto = getRegistrationDto("alisa@gmail.com", "Qaz2@34dff", "Qaz2@34dff");
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
        registrationDto.setRepeatPassword(PASSWORDFAIL);
        assertFalse(passwordValidator.isValid(registrationDto, null));
    }

    private UserRegistrationDto getRegistrationDto(String email,
                                                          String password,
                                                          String repeatpassword) {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(email);
        registrationDto.setPassword(password);
        registrationDto.setRepeatPassword(password);
        return registrationDto;
    }
}
