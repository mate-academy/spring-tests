package mate.academy.dao;

import static mate.academy.model.Role.RoleName.ADMIN;
import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "valid@i.ua";
    private static final String PASSWORD = "1234";
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class, User.class};
    }

    @BeforeEach
    void setUp() {
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        Role adminRole = new Role();
        adminRole.setRoleName(ADMIN);
        final Role adminRoleFromDb = roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName(USER);
        final Role userRoleFromDb = roleDao.save(userRole);

        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(adminRoleFromDb, userRoleFromDb));
    }

    @Test
    @Order(1)
    void save_validUser_ok() {
        User savedUser = userDao.save(user);
        assertNotNull(savedUser);
        assertEquals(1L, savedUser.getId(),
                "Method should return user with id '%s'"
                        .formatted(1L));
    }

    @Test
    @Order(2)
    void findByEmail_existingEmail_ok() {
        User savedUser = userDao.save(user);
        Optional<User> optionalUser = userDao.findByEmail(EMAIL);
        assertTrue(optionalUser.isPresent(),
                "Method should return optional user '%s' from email '%s'"
                        .formatted(savedUser, EMAIL));
        assertEquals(EMAIL, optionalUser.get().getEmail(),
                "Method should return optional user with email '%s' but returned '%s'"
                        .formatted(EMAIL, optionalUser.get().getEmail()));
    }

    @Test
    @Order(3)
    void findByEmail_notExistingEmail_notOk() {
        String notExistingEmail = "notExist@i.ua";
        userDao.save(user);
        Optional<User> optionalUser = userDao.findByEmail(notExistingEmail);
        assertFalse(optionalUser.isPresent(),
                "Method should return false from not existing email '%s'"
                        .formatted(notExistingEmail));
    }

    @Test
    @Order(4)
    void findById_validId_ok() {
        User savedUser = userDao.save(user);
        Optional<User> optionalUser = userDao.findById(savedUser.getId());
        assertTrue(optionalUser.isPresent(),
                "Method should return optional user '%s' from id '%s'"
                        .formatted(savedUser, savedUser.getId()));
        assertEquals(EMAIL, optionalUser.get().getEmail(),
                "Method should return optional user with email '%s' but returned '%s'"
                        .formatted(EMAIL, optionalUser.get().getEmail()));
    }

    @Test
    @Order(5)
    void findById_notExistingId_notOk() {
        long notExistingId = 2L;
        userDao.save(user);
        Optional<User> optionalUser = userDao.findById(notExistingId);
        assertFalse(optionalUser.isPresent(),
                "Method should return false from id not existing id '%s'"
                        .formatted(notExistingId));
    }
}
