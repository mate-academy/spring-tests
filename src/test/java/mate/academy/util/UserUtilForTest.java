package mate.academy.util;

import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;

public class UserUtilForTest {
    private static final String BORIS_EMAIL = "boris@mate.com";
    private static final String BORIS_PASSWORD = "password1234";
    private static final String NADJA_EMAIL = "nadja@mate.com";
    private static final String NADJA_PASSWORD = "password4321";

    public String getBorisEmail() {
        return BORIS_EMAIL;
    }

    public String getBorisPassword() {
        return BORIS_PASSWORD;
    }

    public String getNadjaEmail() {
        return NADJA_EMAIL;
    }

    public String getNadjaPassword() {
        return NADJA_PASSWORD;
    }

    public Role getAdminRole() {
        return new Role(Role.RoleName.ADMIN);
    }

    public Role getUserRole() {
        return new Role(Role.RoleName.USER);
    }

    public User getUserBoris() {
        User user = new User();
        user.setEmail(BORIS_EMAIL);
        user.setPassword(BORIS_PASSWORD);
        return user;
    }

    public User getUserNadja() {
        User user = new User();
        user.setEmail(NADJA_EMAIL);
        user.setPassword(NADJA_PASSWORD);
        return user;
    }
}
