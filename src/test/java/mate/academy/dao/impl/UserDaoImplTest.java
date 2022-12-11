package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

class UserDaoImplTest extends AbstractTest {
    private static UserDao userDao;
    private static RoleDao roleDao;
    private User user;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeAll
    static void beforeAll() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @BeforeEach
    void setUp() {
        role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user = createRawUser(role);
    }

    @Test
    void saveUser_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(user, actual);
    }

    private User createRawUser(Role role) {
        User user = new User();
        user.setEmail("user1@gmail.com");
        user.setPassword("12345678");
        user.setRoles(Set.of(role));
        return user;
    }

}