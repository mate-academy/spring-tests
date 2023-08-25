package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordValidatorTest {
    private static String VALID_PASSWORD = "@W#E$R%Tkjh";
    private static String INVALID_PASSWORD = "";
    private static ConstraintValidatorContext constraintValidatorContext;
    private static PasswordValidator passwordValidator;
    private static UserRegistrationDto registrationDto;

    @BeforeAll
    static void beforeAll() {
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordValidator();
        ReflectionTestUtils.setField(passwordValidator, "field", "password");
        ReflectionTestUtils.setField(passwordValidator, "fieldMatch", "repeatPassword");
    }

    @BeforeEach
    void setUp() {
        registrationDto = new UserRegistrationDto();
    }

    @Test
    void isValid_Ok() {
        registrationDto.setPassword(VALID_PASSWORD);
        registrationDto.setRepeatPassword(VALID_PASSWORD);
        Assertions.assertTrue(passwordValidator.isValid(
                registrationDto, constraintValidatorContext));
    }

    @Test
    void isValid_notOk() {
        registrationDto.setPassword(VALID_PASSWORD);
        registrationDto.setRepeatPassword(INVALID_PASSWORD);
        Assertions.assertFalse(passwordValidator.isValid(
                registrationDto, constraintValidatorContext));

        registrationDto.setPassword(null);
        registrationDto.setRepeatPassword(null);
        Assertions.assertFalse(passwordValidator.isValid(
                registrationDto, constraintValidatorContext));
    }
}
