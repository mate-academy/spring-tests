package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private static final String NOT_EXISTED_EMAIL = "alice@i.ua";
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        User actual = getUser(EMAIL, PASSWORD);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void save_DataProcessingException() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(null));
    }

    @Test
    void findByEmail_existedUser_ok() {
        User expected = getUser(EMAIL, PASSWORD);
        Optional<User> actual = userDao.findByEmail(expected.getEmail());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.get().getPassword());
        Assertions.assertTrue(expected.getRoles().equals(actual.get().getRoles()));
    }

    @Test
    void findByEmail_notExistedUser_ok() {
        Assertions.assertEquals(Optional.empty(), userDao.findByEmail(NOT_EXISTED_EMAIL));
    }

    @Test
    void findById_userExisted_ok() {
        User expected = getUser(EMAIL, PASSWORD);
        Optional<User> actual = userDao.findById(expected.getId());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.get().getPassword());
    }

    @Test
    void findById_notExistedUser_ok() {
        Assertions.assertEquals(Optional.empty(), userDao.findById(1L));
    }

    private User getUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Role role = new Role(Role.RoleName.ADMIN);
        roleDao.save(role);
        user.setRoles(Set.of(role));
        return userDao.save(user);
    }
}
