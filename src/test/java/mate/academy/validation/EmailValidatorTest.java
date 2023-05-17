package mate.academy.validation;

import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailValidatorTest {
    @InjectMocks
    private EmailValidator emailValidator;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void emailIsValid() {
        String email = "den.pugovkin@gmail.com";
        Assertions.assertTrue(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void emailIsNotValid() {
        String email = "den.pugovkingmail.com";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }

    @Test
    void emailIsNotValid_2() {
        String email = "den.pugovkin@gmailcom";
        Assertions.assertFalse(emailValidator.isValid(email, constraintValidatorContext));
    }
}
