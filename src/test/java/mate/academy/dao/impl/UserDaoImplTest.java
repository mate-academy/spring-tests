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
    private static final String EMAIL = "email@com.ua";
    private static final String PASSWORD = "password";
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    public void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    public void save_correctUser_ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    public void save_null_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(null));
    }

    @Test
    public void findByEmail_Ok() {
        Role role = new Role(Role.RoleName.USER);
        role = roleDao.save(role);
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
        userDao.save(user);
        User actual = userDao.findByEmail(EMAIL).get();
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertEquals(Role.RoleName.USER, user.getRoles()
                .stream()
                .findFirst()
                .get()
                .getRoleName());
    }

    @Test
    public void findByEmail_wrongEmail_notOk() {
        Role role = new Role(Role.RoleName.USER);
        role = roleDao.save(role);
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
        userDao.save(user);
        Assertions.assertEquals(Optional.empty(), userDao.findByEmail("wrongEmail@com.ua"));

    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
