package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String PASSWORD = "12345";
    private static UserRegistrationDto userDto;
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void setUp() {
        Password constraintAnnotation = Mockito.mock(Password.class);
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator = new PasswordValidator();
        passwordValidator.initialize(constraintAnnotation);
        userDto = new UserRegistrationDto();
    }

    @Test
    void isValid_Ok() {
        userDto.setPassword(PASSWORD);
        userDto.setRepeatPassword(PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(userDto, constraintValidatorContext));
    }
}
