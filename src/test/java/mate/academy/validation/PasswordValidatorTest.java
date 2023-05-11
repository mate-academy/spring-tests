package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private String email = "bob@i.ua";
    private String password = "123456";
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext constraintValidatorContext;
    private Password constraintAnnotation;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        constraintAnnotation = Mockito.mock(Password.class);
        passwordValidator = new PasswordValidator();
        userRegistrationDto = new UserRegistrationDto();
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setEmail(email);
        userRegistrationDto.setPassword(password);
        userRegistrationDto.setRepeatPassword(password);
        Assertions.assertTrue(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_wrongPassword_NotOk() {
        userRegistrationDto.setPassword("wrongPass");
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_nullPassword_NotOk() {
        userRegistrationDto.setPassword(null);
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }
}
