package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private UserDao userDao;
    private User firstUser;
    private User secondUser;
    private Role userRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = roleDao.save(new Role(Role.RoleName.USER));
        adminRole = roleDao.save(new Role(Role.RoleName.ADMIN));
        firstUser = new User("test@gmail.com", "validPassword");
        secondUser = new User("secondtest@gmail.com", "secondValidPassword");
    }

    @Test
    void save_validUser_ok() {
        User actual = userDao.save(firstUser);
        Long expected = 1L;
        assertNotNull(actual);
        assertEquals(expected, actual.getId());
    }

    @Test
    void save_twoUsers_ok() {
        Long firstExpected = 1L;
        Long secondExpected = 2L;
        User firstSaved = userDao.save(firstUser);
        User secondSaved = userDao.save(secondUser);
        assertEquals(firstExpected, firstSaved.getId());
        assertEquals(secondExpected, secondSaved.getId());
    }

    @Test
    void save_userSavedTwice_notOk() {
        userDao.save(firstUser);
        assertThrows(DataProcessingException.class, () -> userDao.save(firstUser));

    }

    @Test
    void save_userWithId_ok() {
        firstUser.setId(1L);
        User actual = userDao.save(firstUser);
        Long expected = 1L;
        assertEquals(expected, actual.getId());

    }

    @Test
    void findByEmail_validEmail_ok() {
        firstUser.setRoles(Set.of(userRole));
        userDao.save(firstUser);
        Optional<User> actual = userDao.findByEmail(firstUser.getEmail());
        if (actual.isEmpty()) {
            fail("User from Db is empty");
        }
        assertEquals(firstUser.getId(), actual.get().getId());
        assertEquals(firstUser.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_twoUsersInDB_ok() {
        firstUser.setRoles(Set.of(userRole));
        secondUser.setRoles(Set.of(userRole));
        userDao.save(firstUser);
        userDao.save(secondUser);
        Optional<User> actual = userDao.findByEmail(firstUser.getEmail());
        if (actual.isEmpty()) {
            fail("User from Db is empty");
        }
        assertEquals(firstUser.getId(), actual.get().getId());
        assertEquals(firstUser.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_emptyDB_ok() {
        Optional<User> actual = userDao.findByEmail(firstUser.getEmail());
        Optional<User> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void findByEmail_invalidEmail_ok() {
        firstUser.setRoles(Set.of(userRole));
        userDao.save(firstUser);
        Optional<User> actual = userDao.findByEmail("invalidEmail");
        Optional<User> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void findById_invalidEmail_ok() {
        firstUser.setRoles(Set.of(userRole));
        userDao.save(firstUser);
        Optional<User> actual = userDao.findById(firstUser.getId());
        if (actual.isEmpty()) {
            fail("User from Db is empty");
        }
        assertEquals(firstUser.getId(), actual.get().getId());
        assertEquals(firstUser.getEmail(), actual.get().getEmail());
    }

    @Test
    void findById_twoUsersInDB_ok() {
        firstUser.setRoles(Set.of(userRole));
        secondUser.setRoles(Set.of(userRole));
        userDao.save(firstUser);
        userDao.save(secondUser);
        Optional<User> actual = userDao.findById(firstUser.getId());
        if (actual.isEmpty()) {
            fail("User from Db is empty");
        }
        assertEquals(firstUser.getId(), actual.get().getId());
        assertEquals(firstUser.getEmail(), actual.get().getEmail());
    }

    @Test
    void findById_invalidId_ok() {
        firstUser.setRoles(Set.of(userRole));
        userDao.save(firstUser);
        Optional<User> actual = userDao.findById(3L);
        Optional<User> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void findById_emptyDB_ok() {
        Optional<User> actual = userDao.findById(1L);
        Optional<User> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }
}
