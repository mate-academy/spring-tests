package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String PASSWORD = "123456789";
    private static final String EMAIL = "bob@gmail.com";
    private static final String INVALID_PASSWORD = "invalidPassword";
    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private UserRegistrationDto userRegistrationDto;
    private Password constraintAnnotation;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        userRegistrationDto = new UserRegistrationDto();

        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);

        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setRepeatPassword(PASSWORD);
    }

    @Test
    void isValid_Ok() {
        boolean isValid = passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertTrue(isValid);
    }

    @Test
    void isValid_NotOk() {
        userRegistrationDto.setRepeatPassword(INVALID_PASSWORD);
        boolean isValid = passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertFalse(isValid);
    }
}
