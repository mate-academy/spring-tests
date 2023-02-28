package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        String email = "bchupika@mate.academy";
        String password = "12345678";
        expectedUser = new User();
        expectedUser.setEmail(email);
        expectedUser.setPassword(password);
        expectedUser.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
    }

    @Test
    void isValid_validData_ok() {
        assertTrue(emailValidator.isValid(expectedUser.getEmail(), constraintValidatorContext));
    }

    @Test
    void isValid_invalidEmail_notOk() {
        assertFalse(emailValidator.isValid("invalidEmail", constraintValidatorContext));
        assertFalse(emailValidator.isValid("12@33.43", constraintValidatorContext));
        assertFalse(emailValidator.isValid("@invalid.email", constraintValidatorContext));
        assertFalse(emailValidator.isValid("invalid @email.com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("invalid.email@.com", constraintValidatorContext));
        assertFalse(emailValidator.isValid("", constraintValidatorContext));
        assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}
