package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String FISTS_PASSWORD = "1234";
    private static final String SECOND_PASSWORD = "4321";
    private static PasswordValidator validator;
    private static ConstraintValidatorContext context;

    @BeforeAll
    static void beforeAll() {
        validator = new PasswordValidator();
        Password annotation = Mockito.mock(Password.class);
        Mockito.when(annotation.field()).thenReturn("password");
        Mockito.when(annotation.fieldMatch()).thenReturn("repeatPassword");
        validator.initialize(annotation);
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_matchingPasswordsValid_ok() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setPassword(FISTS_PASSWORD);
        dto.setRepeatPassword(FISTS_PASSWORD);
        Assertions.assertTrue(validator.isValid(dto, context),
                "Expected same passwords to be valid in dto");
    }

    @Test
    void isValid_differentPasswordsValid_notOK() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setPassword(SECOND_PASSWORD);
        dto.setRepeatPassword(FISTS_PASSWORD);
        Assertions.assertFalse(validator.isValid(dto, context),
                "Expected different passwords to be valid in dto");
    }
}
