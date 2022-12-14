package mate.academy.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidatorContext;
import mate.academy.model.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PasswordValidatorTest {
    private static PasswordValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static List<UserRegistrationDto> correctUserDtoList;
    private static List<UserRegistrationDto> incorrectUserDtoList;

    @BeforeAll
    static void setUp() {
        passwordValidator = new PasswordValidator();
        Password password = Mockito.spy(Password.class);
        Mockito.when(password.field()).thenReturn("password");
        Mockito.when(password.fieldMatch()).thenReturn("repeatPassword");
        passwordValidator.initialize(password);
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        correctUserDtoList = fillCorrectUserRegistrationDto();
        incorrectUserDtoList = fillIncorrectUserRegistrationDto();
    }

    private static List<UserRegistrationDto> fillIncorrectUserRegistrationDto() {
        List<UserRegistrationDto> userDtoList = new ArrayList<>();
        List<String> passwordsList = List.of("1234", "fgsfdgs52345", "JKHG*&YOI$KFgsfg", "123abc");
        List<String> mismatchPaswordsList = List.of("fg", "45gfd", "8934hf", "45g");
        for (int i = 0; i < passwordsList.size(); i++) {
            userDtoList.add(createUserRegistrationDto(passwordsList.get(i),
                    mismatchPaswordsList.get(i)));
        }
        return userDtoList;
    }

    private static List<UserRegistrationDto> fillCorrectUserRegistrationDto() {
        List<String> passwordsList = List.of("1234", "fgsfdgs52345", "JKHG*&YOI$KFgsfg", "123abc");
        List<UserRegistrationDto> userDtoList = new ArrayList<>();
        passwordsList.forEach(s -> userDtoList.add(createUserRegistrationDto(s, s)));
        return userDtoList;
    }

    private static UserRegistrationDto createUserRegistrationDto(String password,
                                                                 String repeatPassword) {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(password);
        userRegistrationDto.setRepeatPassword(repeatPassword);
        return userRegistrationDto;
    }

    @Test
    void isValid_NotOk() {
        String message = "When passwords are not the same, you must return false";
        incorrectUserDtoList.forEach(dto ->
                assertFalse(passwordValidator.isValid(dto, constraintValidatorContext), message));
    }

    @Test
    void isValid_Ok() {
        String message = "When passwords are the same you must return true";
        correctUserDtoList.forEach(dto ->
                assertTrue(passwordValidator.isValid(dto, constraintValidatorContext), message));
    }
}
