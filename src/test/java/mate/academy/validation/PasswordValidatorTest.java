package mate.academy.validation;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String VALID_PASSWORD = "Matebro1223";
    private static final String CONFIRM_PASSWORD = "Matebro1223";
    private static final String INVALID_PASSWORD = "45325";
    private UserRegistrationDto registrationDto;
    private ConstraintValidator<Password, UserRegistrationDto> passwordValidator =
            new PasswordValidator();
    private ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

    @BeforeEach
    void setUp() {
        registrationDto = new UserRegistrationDto();
        setField(passwordValidator, "field", "password");
        setField(passwordValidator, "fieldMatch", "repeatPassword");
        registrationDto.setPassword(VALID_PASSWORD);
        registrationDto.setRepeatPassword(CONFIRM_PASSWORD);
    }

    @Test
    void passwordValid_Ok() {
        boolean isValid = passwordValidator.isValid(registrationDto, context);
        Assertions.assertTrue(isValid);
    }

    @Test
    void passwordValid_notOk() {
        registrationDto.setRepeatPassword(INVALID_PASSWORD);
        boolean isInvalid = passwordValidator.isValid(registrationDto, context);
        Assertions.assertFalse(isInvalid);
    }
}
