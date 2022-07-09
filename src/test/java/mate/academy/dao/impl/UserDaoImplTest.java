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
    private static final String EMAIL = "test@gmail.com";
    private static final String PASSWORD = "123qwe";
    private static final long ID = 1L;
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(new Role(Role.RoleName.USER));
        roleDao.save(new Role(Role.RoleName.ADMIN));
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(roleDao.getRoleByName(Role.RoleName.USER.name()).get()));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @Test
    void save_ok() {
        User expected = user;
        User actual = userDao.save(user);
        Assertions.assertEquals(ID, actual.getId());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getRoles(), actual.getRoles());
    }

    @Test
    void save_nullUser_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.save(null));
    }

    @Test
    void findByEmail_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(ID, actual.get().getId());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
        Assertions.assertEquals(Set.of(ROLE), actual.get().getRoles());
    }

    @Test
    void findByEmail_nullEmail_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.save(null));
    }

    @Test
    void findById_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
        Assertions.assertEquals(Set.of(ROLE), actual.get().getRoles());
    }

    @Test
    void findById_notExistingId_notOk() {
        Assertions.assertEquals(Optional.empty(), userDao.findById(2L));
    }
}
