package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        Password annotation = Mockito.mock(Password.class);
        Mockito.when(annotation.field()).thenReturn("password");
        Mockito.when(annotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(annotation);

        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("bob@mail.com");
        userRegistrationDto.setPassword("bobNumber1");
        userRegistrationDto.setRepeatPassword("bobNumber1");
    }

    @Test
    void isValid_Ok() {
        boolean actual = passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_nullPassword_notOk() {
        userRegistrationDto.setPassword(null);
        boolean nullPassword =
                passwordValidator.isValid(userRegistrationDto, constraintValidatorContext);
        Assertions.assertFalse(nullPassword);
    }

    @Test
    void isValid_badPassword_notOk() {
        userRegistrationDto.setRepeatPassword("aliceNumber1");
        boolean badPassword = passwordValidator.isValid(userRegistrationDto,
                constraintValidatorContext);
        Assertions.assertFalse(badPassword);
    }
}
