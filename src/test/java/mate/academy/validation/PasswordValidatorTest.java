package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordValidatorTest {
    private static final String SAME_PASSWORD = "sdsdds";
    private static final String OTHER_PASSWORD = "hwej";
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
        dto.setPassword(SAME_PASSWORD);
        dto.setRepeatPassword(SAME_PASSWORD);
        assertTrue(validator.isValid(dto, context),
                "Expected same passwords to be valid in dto");
    }

    @Test
    void isValid_differentPasswordsValid_notOK() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setPassword(SAME_PASSWORD);
        dto.setRepeatPassword(OTHER_PASSWORD);
        assertFalse(validator.isValid(dto, context),
                "Expected different passwords to be valid in dto");
    }
}
