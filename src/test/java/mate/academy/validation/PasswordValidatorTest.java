package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "11111111";
    private static final String REPEAT_PASSWORD = "11111111";
    private static final String WRONG_PASSWORD = "000000000000";
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static Password constraintAnnotation;
    private static UserRegistrationDto registrationDto;

    @BeforeAll
    static void beforeAll() {
        registrationDto = new UserRegistrationDto();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        constraintAnnotation = Mockito.mock(Password.class);
        Mockito.when(constraintAnnotation.field()).thenReturn("password");
        Mockito.when(constraintAnnotation.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @BeforeEach
    void setUp() {
        registrationDto.setPassword(PASSWORD);
        registrationDto.setEmail(EMAIL);
        registrationDto.setRepeatPassword(REPEAT_PASSWORD);
    }

    @Test
    void isValid_ok() {
        boolean actual = passwordValidator.isValid(registrationDto, constraintValidatorContext);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_notOk() {
        registrationDto.setRepeatPassword(WRONG_PASSWORD);
        boolean actual = passwordValidator.isValid(registrationDto, constraintValidatorContext);
        Assertions.assertFalse(actual);
    }
}
