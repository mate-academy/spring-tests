package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "bob@domain.com";
    private static final String PASSWORD = "1234";
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user = createUser(role);
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(user, actual);
    }

    @Test
    void save_NotUniqueEmails_NotOk() {
        userDao.save(user);
        DataProcessingException thrown = Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.save(user));
        Assertions.assertEquals("Can't create entity: " + user, thrown.getMessage());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_NotExistentUserInDb_NotOk() {
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(user.getId());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getId(), actual.get().getId());
    }

    @Test
    void findById_NotExistentIdInDb_NotOk() {
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertTrue(actual.isEmpty());
    }

    private User createUser(Role role) {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
        return user;
    }
}
