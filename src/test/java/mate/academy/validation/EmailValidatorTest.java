package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String FIRST_EMAIL = "bob@i.ua";
    private static final String SECOND_EMAIL = "alice@gmail.com";
    private static final String THIRD_EMAIL = null;

    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeAll
    static void beforeAll() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_Ok() {
        boolean firstEmailValidator =
                emailValidator.isValid(FIRST_EMAIL, constraintValidatorContext);
        boolean secondEmailValidator =
                emailValidator.isValid(SECOND_EMAIL, constraintValidatorContext);
        boolean thirdEmailValidator =
                emailValidator.isValid(THIRD_EMAIL, constraintValidatorContext);

        Assertions.assertTrue(firstEmailValidator);
        Assertions.assertTrue(secondEmailValidator);
        Assertions.assertFalse(thirdEmailValidator);
    }
}
