package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest{
    private static User user;
    private static User admin;
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        Role userRole = roleDao.save(new Role(Role.RoleName.USER));
        Role adminRole = roleDao.save(new Role(Role.RoleName.ADMIN));
        user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("user_password");
        user.setRoles(Set.of(userRole));
        admin = new User();
        admin.setEmail("admin@mail.com");
        admin.setPassword("admin_password");
        admin.setRoles(Set.of(userRole, adminRole));
    }

    @Test
    void save_Ok() {
        User actualUser = userDao.save(user);
        assertNotNull(actualUser);
        assertEquals(1L, actualUser.getId());
        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getPassword(), actualUser.getPassword());
        assertEquals(user.getRoles().size(), actualUser.getRoles().size());
        assertTrue(actualUser.getRoles().containsAll(user.getRoles()));

        actualUser = userDao.save(admin);
        assertNotNull(actualUser);
        assertEquals(2L, actualUser.getId());
        assertEquals(admin.getEmail(), actualUser.getEmail());
        assertEquals(admin.getPassword(), actualUser.getPassword());
        assertEquals(admin.getRoles().size(), actualUser.getRoles().size());
        assertTrue(actualUser.getRoles().containsAll(admin.getRoles()));
    }

    @Test
    void save_nullUser_notOk() {
        assertThrows(DataProcessingException.class, () -> userDao.save(null));
    }

    @Test
    void findById_Ok() {
        user = userDao.save(user);
        admin = userDao.save(admin);
        Optional<User> actualUser = userDao.findById(user.getId());
        assertNotNull(actualUser);
        assertTrue(actualUser.isPresent());
        assertEquals(user.getId(), actualUser.get().getId());
        assertEquals(user.getEmail(), actualUser.get().getEmail());
        assertEquals(user.getPassword(), actualUser.get().getPassword());

        actualUser = userDao.findById(admin.getId());
        assertNotNull(actualUser);
        assertTrue(actualUser.isPresent());
        assertEquals(admin.getId(), actualUser.get().getId());
        assertEquals(admin.getEmail(), actualUser.get().getEmail());
        assertEquals(admin.getPassword(), actualUser.get().getPassword());
    }

    @Test
    void findById_nonExist_notOk() {
        Optional<User> actualUser = userDao.findById(1L);
        assertNotNull(actualUser);
        assertTrue(actualUser.isEmpty());
    }

    @Test
    void findById_nullId_notOk() {
        assertThrows(DataProcessingException.class, () -> userDao.findById(null));
    }

    @Test
    void findByEmail_Ok() {
        user = userDao.save(user);
        admin = userDao.save(admin);
        Optional<User> actualUser = userDao.findByEmail(user.getEmail());
        assertNotNull(actualUser);
        assertTrue(actualUser.isPresent());
        assertEquals(user.getId(), actualUser.get().getId());
        assertEquals(user.getEmail(), actualUser.get().getEmail());
        assertEquals(user.getPassword(), actualUser.get().getPassword());
        assertEquals(user.getRoles().size(), actualUser.get().getRoles().size());

        actualUser = userDao.findByEmail(admin.getEmail());
        assertNotNull(actualUser);
        assertTrue(actualUser.isPresent());
        assertEquals(admin.getId(), actualUser.get().getId());
        assertEquals(admin.getEmail(), actualUser.get().getEmail());
        assertEquals(admin.getPassword(), actualUser.get().getPassword());
        assertEquals(admin.getRoles().size(), actualUser.get().getRoles().size());
    }

    @Test
    void findByEmail_nonExist_notOk() {
        Optional<User> actualUser = userDao.findByEmail(user.getEmail());
        assertNotNull(actualUser);
        assertTrue(actualUser.isEmpty());
    }

    @Test
    void findByEmail_nullId_notOk() {
        Optional<User> actualUser = userDao.findByEmail(null);
        assertNotNull(actualUser);
        assertTrue(actualUser.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        Class[] classes = {Role.class, User.class};
        return classes;
    }
}