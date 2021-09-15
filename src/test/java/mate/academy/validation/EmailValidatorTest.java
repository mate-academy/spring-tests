package mate.academy.validation;

import java.util.List;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private EmailValidator emailValidator = new EmailValidator();
    private ConstraintValidatorContext context;
    private static final List<String> CORRECT_EMAILS = List.of("dana.khromenko@gmail.com",
            "lisa_simpson@yandex.ru", "mary123@big.net");
    private static final List<String> INCORRECT_EMAILS = List.of("dana#123@gmail.com",
            "lisa_simpson@ru$.", "mary123_big.net");

    @BeforeEach
    void setUp() {
        context = Mockito.mock(ConstraintValidatorContextImpl.class);
    }

    @Test
    void isValid_Ok() {
        for (String email : CORRECT_EMAILS) {
            if (!emailValidator.isValid(email, context)) {
                Assertions.fail("E-mail " + email + " must be valid");
            }
        }
    }

    @Test
    void isValid_NotOk() {
        for (String email : INCORRECT_EMAILS) {
            if (emailValidator.isValid(email, context)) {
                Assertions.fail("E-mail " + email + " must be not valid");
            }
        }
    }
}
