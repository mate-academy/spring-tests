package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "batman@ukr.net";
    private static final String PASSWORD = "1234";
    private Role roleUser;
    private RoleDao roleDao;
    private UserDao userDao;
    private User user;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        roleUser = new Role(Role.RoleName.USER);
        roleDao.save(roleUser);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(roleUser));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
        assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findUserByEmail_emailExist_Ok() {
        User savedUserInDb = userDao.save(user);
        User actual = userDao.findByEmail(EMAIL).get();
        assertEquals(1L, actual.getId());
        assertEquals(savedUserInDb.getEmail(), actual.getEmail());
        assertEquals(savedUserInDb.getPassword(), actual.getPassword());
    }

    @Test
    void findUserByEmail_emailNotExist_NotOk() {
        userDao.save(user);
        Optional<User> userDaoByEmail = userDao.findByEmail("joker@ukr.net");
        assertEquals(Optional.empty(), userDaoByEmail);
    }

    @Test
    void findUserById_Ok() {
        userDao.save(user);
        Optional<User> optionalUser = userDao.findById(1L);
        assertTrue(optionalUser.isPresent());
        assertEquals(user.getEmail(), optionalUser.get().getEmail());
        assertEquals(user.getPassword(), optionalUser.get().getPassword());

    }

    @Test
    void findUserById_idNotExist_NotOk() {
        userDao.save(user);
        Optional<User> optionalUser = userDao.findById(100L);
        assertEquals(Optional.empty(), optionalUser);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }
}

