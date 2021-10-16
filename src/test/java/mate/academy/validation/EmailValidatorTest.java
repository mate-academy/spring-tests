package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String EMAIL = "bob@mail.ua";
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContextImpl.class);
    }

    @Test
    void isValidOk() {
        Assertions.assertTrue(emailValidator.isValid(EMAIL, constraintValidatorContext));
    }
}
