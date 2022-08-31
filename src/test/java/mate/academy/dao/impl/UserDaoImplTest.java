package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final Long FIRST_INDEX_ID = 1L;
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "1234";
    private User user;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @Test
    void saveUser_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(FIRST_INDEX_ID, actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    void findByEmail_ok() {
        User expected = userDao.save(user);
        Optional<User> actual = userDao.findByEmail(expected.getEmail());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail());
    }
}
