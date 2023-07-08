package mate.academy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static final String TEST_PASSWORD_OK = "12345678";
    private ConstraintValidator<Password, UserRegistrationDto> passwordValidator;
    private UserRegistrationDto userRegistrationDto;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        passwordValidator = new PasswordValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
        userRegistrationDto.setPassword(TEST_PASSWORD_OK);
        userRegistrationDto.setRepeatPassword(TEST_PASSWORD_OK);
    }

    @Test
    void isValid_Ok() {
        boolean actual = passwordValidator.isValid(userRegistrationDto, context);
        Assertions.assertTrue(actual);
    }

    @Test
    void isValid_Not_Ok() {
        userRegistrationDto.setRepeatPassword("Wrong password");
        boolean actual = passwordValidator.isValid(userRegistrationDto, context);
        Assertions.assertFalse(actual);
    }
}
