package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String FIELD = "password";
    private static final String FIELD_MATCH = "repeatPassword";
    private final ConstraintValidatorContext context =
            Mockito.mock(ConstraintValidatorContext.class);
    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        Password constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn(FIELD);
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn(FIELD_MATCH);
        passwordValidator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_validFields_ok() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setPassword("mySecretPassword");
        userDto.setRepeatPassword("mySecretPassword");

        Assertions.assertTrue(passwordValidator.isValid(userDto, context));
    }

    @Test
    void isValid_incorrectFields_ok() {
        UserRegistrationDto firstUserDto = new UserRegistrationDto();
        firstUserDto.setPassword("myCorrectPassword");
        firstUserDto.setRepeatPassword("myIncorrectPassword");
        UserRegistrationDto secondUserDto = new UserRegistrationDto();
        secondUserDto.setPassword("MyPassword");
        secondUserDto.setRepeatPassword(null);
        UserRegistrationDto thirdUserDto = new UserRegistrationDto();
        thirdUserDto.setPassword(null);
        thirdUserDto.setRepeatPassword("MyPassword");

        Assertions.assertFalse(passwordValidator.isValid(firstUserDto, context));
        Assertions.assertFalse(passwordValidator.isValid(secondUserDto, context));
        Assertions.assertFalse(passwordValidator.isValid(thirdUserDto, context));
    }
}
