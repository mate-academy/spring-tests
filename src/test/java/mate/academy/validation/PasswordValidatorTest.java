package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private String userEmail;
    private String userPassword;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto registrationDto;
    private PasswordValidator passwordValidator;
    private Password constraintAnnotation;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        registrationDto = new UserRegistrationDto();
        userPassword = "12345";
        userEmail = "user@gmail.com";
        registrationDto.setEmail(userEmail);
        registrationDto.setPassword(userPassword);

        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_validPassword_OK() {
        registrationDto.setRepeatPassword(userPassword);
        Assertions.assertTrue(passwordValidator.isValid(
                registrationDto, constraintValidatorContext), "Passwords should match");
    }

    @Test
    void isValid_invalidPassword_Ok() {
        registrationDto.setRepeatPassword(userPassword + "1");
        Assertions.assertFalse(passwordValidator.isValid(
                registrationDto, constraintValidatorContext), "Passwords should NOT match");
    }
}
