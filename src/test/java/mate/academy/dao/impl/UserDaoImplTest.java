package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private Role userRole;
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        roleDao.save(userRole);
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void saveUser_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
        User savedUser = userDao.save(user);
        Assertions.assertEquals(1L, savedUser.getId());
        user.setId(1L);
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    void findByEmail_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
        userDao.save(user);
        Optional<User> optionalUser = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals(user, optionalUser.get());
    }

    @Test
    void findByEmail_invalidEmail_NotOk() {
        Assertions.assertTrue(userDao.findByEmail("alice@i.ua").isEmpty());
    }

    @Test
    void getById_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
        userDao.save(user);
        Optional<User> optionalUser = userDao.findById(1L);
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals(user.getEmail(), EMAIL);
        Assertions.assertEquals(user.getPassword(), PASSWORD);
    }

    @Test
    void getById_invalidId_NotOk() {
        Assertions.assertTrue(userDao.findById(100L).isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
