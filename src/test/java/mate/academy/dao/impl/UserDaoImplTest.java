package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Set;

class UserDaoImplTest extends AbstractTest{
    private static final String EMAIL = "hello@i.am";
    private static final String PASSWORD = "1221";
    private UserDao userDao;
    private RoleDao roleDao;
    private User hello;
    private Role saveRole;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        hello = new User();
        hello.setEmail(EMAIL);
        hello.setPassword(PASSWORD);
        saveRole = roleDao.save(new Role(Role.RoleName.USER));
        hello.setRoles(Set.of(saveRole));
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(hello);
        Optional<User> saveHello = userDao.findByEmail(EMAIL);
        Assertions.assertNotNull(saveHello);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }
}