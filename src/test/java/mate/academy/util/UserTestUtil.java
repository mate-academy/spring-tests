package mate.academy.util;

import mate.academy.model.Role;
import mate.academy.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserTestUtil {
    public final static String EMAIL = "bob@gmail.com";
    public final static String PASSWORD = "12345678";
    public final static String INCORRECT_EMAIL = "123aaa@678bbb";


    public static List<String> getListOfStringRoles(User user) {
        return user.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .collect(Collectors.toList());
    }

    public static User getUserBob() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        Role userRole = new Role(Role.RoleName.USER);
        user.setRoles(Set.of(userRole));
        return user;
    }
    
    public static Role getUserRole() {
        Role expected = new Role();
        expected.setRoleName(Role.RoleName.USER);
        return expected;
    }
}
