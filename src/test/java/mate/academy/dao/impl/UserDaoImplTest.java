package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final Long ID_1 = 1L;
    private UserDao userDao;
    private User user;
    private Role role;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail("valid@i.ua");
        user.setPassword("abcd1234");
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_validUser_ok() {
        User expected = user;
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID_1, actual.getId());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getRoles(), actual.getRoles());
    }

    @Test
    void save_nullData_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.save(null));
    }

    @Test
    void findByEmail_validEmail_ok() {
        User expected = userDao.save(user);
        User actual = userDao.findByEmail(expected.getEmail()).get();
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(1, actual.getRoles().size());
        Assertions.assertEquals(Role.RoleName.USER, user.getRoles().stream()
                .findFirst().get().getRoleName());
    }

    @Test
    void findByEmail_nullEmail_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> userDao.findByEmail(null).get());
    }

    @Test
    void findById_validId_ok() {
        User expected = userDao.save(user);
        User actual = userDao.findById(expected.getId()).get();
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    void findById_notValidId_notOk() {
        userDao.save(user);
        Assertions.assertEquals(Optional.empty(), userDao.findById(2L));
    }
}
