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
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(new Role(Role.RoleName.USER));
        roleDao.save(new Role(Role.RoleName.ADMIN));
    }

    @Test
    void save_ok() {
        User user = new User();
        String email = "email@com.ua";
        String password = "123456789";
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(roleDao.getRoleByName(Role.RoleName.USER.name()).get()));
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void save_nullInput_notOk() {
        try {
            userDao.save(null);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: null", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void findByEmail_ok() {
        User user = new User();
        String email = "email@com.ua";
        String password = "123456789";
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(roleDao.getRoleByName(Role.RoleName.USER.name()).get()));
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(email);
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(password, actual.get().getPassword());
        Assertions.assertEquals(email, actual.get().getEmail());
    }

    @Test
    void findByEmail_wrongEmail_notOk() {
        User user = new User();
        String email = "email@com.ua";
        String password = "123456789";
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(roleDao.getRoleByName(Role.RoleName.USER.name()).get()));
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail("qwerw");
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_ok() {
        User user = new User();
        String email = "email@com.ua";
        String password = "123456789";
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(roleDao.getRoleByName(Role.RoleName.USER.name()).get()));
        User savedUser = userDao.save(user);
        Optional<User> actual = userDao.findById(savedUser.getId());
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(email, actual.get().getEmail());
        Assertions.assertEquals(password, actual.get().getPassword());
    }

    @Test
    void findById_wrongId_notOk() {
        User user = new User();
        String email = "email@com.ua";
        String password = "123456789";
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(roleDao.getRoleByName(Role.RoleName.USER.name()).get()));
        userDao.save(user);
        Optional<User> actual = userDao.findById(2L);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }
}
