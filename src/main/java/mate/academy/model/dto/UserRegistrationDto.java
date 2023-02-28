package mate.academy.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mate.academy.validation.Email;
import mate.academy.validation.Password;

@Password(field = "password", fieldMatch = "repeatPassword")
@Getter
@Setter
public class UserRegistrationDto {
    @Email
    private String email;
    @NotEmpty(message = "The password couldn't be empty")
    @Size(min = 8, message = "Password must be at least 8 symbols long")
    private String password;
    private String repeatPassword;
}
