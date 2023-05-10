package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private Password constraintAnnotation;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void passwordValidate_Ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("bob@i.ua");
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword("1234");
        Assertions.assertTrue(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void passwordValidate_differentPasswords_NotOk() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("bob@i.ua");
        userRegistrationDto.setPassword("1234");
        userRegistrationDto.setRepeatPassword("12345");
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto, constraintValidatorContext));
    }
}
