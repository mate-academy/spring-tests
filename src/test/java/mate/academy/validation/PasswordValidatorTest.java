package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static UserRegistrationDto userRegistrationDto;

    @BeforeAll
    static void beforeAll() {
        Password password = Mockito.spy(Password.class);
        passwordValidator = new PasswordValidator();
        userRegistrationDto = new UserRegistrationDto();
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        userRegistrationDto.setEmail("someemail@gmail.com");
        userRegistrationDto.setPassword("password");
        userRegistrationDto.setRepeatPassword("password");
        Assertions.assertTrue(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }

    @Test
    void isValid_NotOk() {
        userRegistrationDto.setEmail("someemail@gmail.com");
        userRegistrationDto.setPassword("password");
        userRegistrationDto.setRepeatPassword("another");
        Assertions.assertFalse(passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext));
    }
}
