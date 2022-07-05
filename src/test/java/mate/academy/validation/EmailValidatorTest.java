package mate.academy.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private ConstraintValidator<Email, String> validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new EmailValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_ok() {
        String firstEmail = "firstEmail@gmail.com";
        String secondEmail = "secondEmail1988@meta.ua";
        String thirdEmail = "12thirdEmail12@ukrposhta.ua";
        String fourthEmail = "fourth444Email@ravlyk.com";

        Assertions.assertTrue(validator.isValid(firstEmail, context));
        Assertions.assertTrue(validator.isValid(secondEmail, context));
        Assertions.assertTrue(validator.isValid(thirdEmail, context));
        Assertions.assertTrue(validator.isValid(fourthEmail, context));
    }

    @Test
    void isValid_invalidEmails_notOk() {
        String firstEmail = "@gmail.com";
        String secondEmail = "";
        String thirdEmail = "1@1";
        String fourthEmail = "fjdksfhskd1231jfbnsdkjbc213";

        Assertions.assertFalse(validator.isValid(firstEmail, context));
        Assertions.assertFalse(validator.isValid(secondEmail, context));
        Assertions.assertFalse(validator.isValid(thirdEmail, context));
        Assertions.assertFalse(validator.isValid(fourthEmail, context));
    }
}
