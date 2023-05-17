package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {

    private static final String EMAIL = "vasyl@gmail.com";
    private static final String PASSWORD = "123456789";
    private static final String WRONG_PASSWORD = "555";
    private ConstraintValidatorContext constraintValidatorContext;
    private PasswordValidator passwordValidator = new PasswordValidator();

    private Password constraintAnnotation;

    @BeforeEach
    void setUp() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    public void is_Valid_Ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(PASSWORD);
        Assertions.assertTrue(passwordValidator
                .isValid(userRegistrationDto,constraintValidatorContext));
    }

    @Test
    public void is_Valid_Not_Ok() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(PASSWORD);
        userRegistrationDto.setRepeatPassword(WRONG_PASSWORD);
        Assertions.assertFalse(passwordValidator
                .isValid(userRegistrationDto,constraintValidatorContext));
    }
}
