package mate.academy.validation;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private static final String VALID_EMAIL = "alice@test.com";

    private static EmailValidator emailValidator;
    private static ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_OK() {
        boolean isValid = emailValidator.isValid(VALID_EMAIL,
                constraintValidatorContext);
        Assertions.assertTrue(isValid);
    }

    @Test
    void isInvalid() {
        List<String> inValidEmails = new ArrayList<>();
        inValidEmails.add("alice");
        inValidEmails.add("alice@");
        inValidEmails.add("alice@+");
        inValidEmails.add("alice@test.");
        inValidEmails.add("alice.@");
        inValidEmails.add("  @test");
        inValidEmails.add(".com");
        inValidEmails.add("  @test.com");

        for (String s:inValidEmails) {
            boolean isValid = emailValidator.isValid(s,
                    constraintValidatorContext);
            Assertions.assertFalse(isValid);
        }
    }
}
