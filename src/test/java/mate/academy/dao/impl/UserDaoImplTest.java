package mate.academy.dao.impl;

import java.util.NoSuchElementException;
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
    private String expectedEmail;
    private User expectedUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        final RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        expectedEmail = "vitaliy@i.ua";
        String expectedPassword = "12345678";
        final Role role = new Role(Role.RoleName.USER);
        expectedUser = new User();
        expectedUser.setEmail(expectedEmail);
        expectedUser.setPassword(expectedPassword);
        expectedUser.setRoles(Set.of(role));
        roleDao.save(role);
    }

    @Test
    void save_ok() {
        User actual = userDao.save(expectedUser);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_ok() {
        userDao.save(expectedUser);
        User actual = userDao.findByEmail(expectedEmail).get();
        Assertions.assertEquals(expectedEmail, actual.getEmail());
    }

    @Test
    void findByEmail_emptyValue_notOk() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                userDao.findByEmail("").get());
    }

    @Test
    void findByEmail_nullValue_notOk() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                userDao.findByEmail(null).get());
    }

    @Test
    void findById_ok() {
        Long expectedId = 1L;
        userDao.save(expectedUser);
        User actual = userDao.findById(expectedId).get();
        Assertions.assertEquals(expectedId, actual.getId());
    }

    @Test
    void findById_tooBigId_notOk() {
        Long id = 2L;
        userDao.save(expectedUser);
        Assertions.assertThrows(NoSuchElementException.class, () ->
                userDao.findById(id).get());
    }

    @Test
    void findById_nullId_notOk() {
        Long id = null;
        userDao.save(expectedUser);
        Assertions.assertThrows(DataProcessingException.class, () ->
                userDao.findById(id).get());
    }
}
