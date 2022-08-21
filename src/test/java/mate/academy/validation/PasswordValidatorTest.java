package mate.academy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static ConstraintValidator<Password, UserRegistrationDto> validator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static UserRegistrationDto userRegistrationDto;

    @BeforeAll
    static void beforeAll() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        userRegistrationDto = new UserRegistrationDto();
        validator = new PasswordValidator();
    }

    @Test
    void isValid_ok() {
        userRegistrationDto.setEmail("user@mail.com");
        userRegistrationDto.setPassword("12345678");
        userRegistrationDto.setRepeatPassword("12345678");
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        validator.initialize(password);
        Assertions.assertTrue(validator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_passwordsDoNotMatch_notOk() {
        userRegistrationDto.setEmail("user@mail.com");
        userRegistrationDto.setPassword("12345678910");
        userRegistrationDto.setRepeatPassword("12345678");
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        validator.initialize(password);
        Assertions.assertFalse(validator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_passwordIsNul_notOk() {
        userRegistrationDto.setEmail("user@mail.com");
        userRegistrationDto.setPassword(null);
        userRegistrationDto.setRepeatPassword(null);
        Password password = Mockito.mock(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        validator.initialize(password);
        Assertions.assertFalse(validator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
