package mate.academy.security;

import mate.academy.exception.AuthenticationException;
import mate.academy.model.User;

public interface AuthenticationService {
    User register(String email, String password);

    User login(String login, String password) throws AuthenticationException;
}
