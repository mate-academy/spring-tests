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
    private static final String PASSWORD = "123456";
    private static final String NOT_EXISTING_EMAIL = "alice@i.ua";
    private static final Long NOT_EXISTING_ID = 10L;
    private static final String EMPTY_EMAIL = "";
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role userRole;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role(Role.RoleName.USER);
        roleDao.save(userRole);

        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @Test
    void save_saveUser_Ok() {
        //arrange
        Long expectedId = 1L;

        //act
        User actual = userDao.save(user);

        //assert
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
        Assertions.assertEquals(expectedId, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void save_saveNull_NotOk() {
        //assert
        Assertions.assertThrows(Exception.class, () -> userDao.save(null));
    }

    @Test
    void findByEmail_validEmail_Ok() {
        //arrange
        userDao.save(user);

        //act
        Optional<User> actual = userDao.findByEmail(EMAIL);

        //assert
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findByEmail_notExistingEmail_NotOk() {
        //assert
        Assertions.assertThrows(Exception.class,
                () -> userDao.findByEmail(NOT_EXISTING_EMAIL).orElseThrow());
    }

    @Test
    void findByEmail_emptyEmail_NotOk() {
        //assert
        Assertions.assertThrows(Exception.class,
                () -> userDao.findByEmail(EMPTY_EMAIL).orElseThrow());
    }

    @Test
    void findByEmail_nullEmail_NotOk() {
        //assert
        Assertions.assertThrows(Exception.class,
                () -> userDao.findByEmail(null).orElseThrow());
    }

    @Test
    void findById_validId_Ok() {
        //arrange
        userDao.save(user).getId();
        Long expected = 1L;

        //act
        Optional<User> actual = userDao.findById(expected);

        //assert
        Assertions.assertEquals(expected, actual.get().getId());
        Assertions.assertEquals(EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findById_notExistingId_NotOk() {
        //assert
        Assertions.assertThrows(Exception.class,
                () -> userDao.findById(NOT_EXISTING_ID).orElseThrow());
    }

    @Test
    void findById_nullId_NotOk() {
        //assert
        Assertions.assertThrows(Exception.class,
                () -> userDao.findById(null).orElseThrow());
    }
}
