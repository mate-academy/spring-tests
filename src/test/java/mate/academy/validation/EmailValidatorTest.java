package mate.academy.validation;

import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintValidatorContext;
import mate.academy.exception.validation.EmailValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EmailValidatorTest {
    private Map<String, Boolean> map;
    private EmailValidator emailValidator;
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        map = new HashMap<>();
        map.put("user.email@com.ua", true);
        map.put("bob@i.ua", true);
        map.put("bob @i.ua", false);
        map.put("wrongemail", false);
        map.put("wrongemail@", false);
        map.put("wrong@email", false);
        map.put("wrong@email.", false);
        map.put(null, false);
        emailValidator = new EmailValidator();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    public void isValid_testMap_ok() {
        Assertions.assertFalse(map.entrySet().stream()
                .anyMatch(e -> emailValidator.isValid(e.getKey(), constraintValidatorContext)
                        != e.getValue()));
    }
}
