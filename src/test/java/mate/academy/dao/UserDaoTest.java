package mate.academy.dao;

import static mate.academy.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.AbstractTest;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private UserDao userDao;
    private Session session;
    private Transaction transaction;
    private User user;
    private String email = "user@gmail.com";
    private String password = "password";
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        session = getSessionFactory().openSession();
        transaction = session.beginTransaction();
        role = new Role(ADMIN);
        session.save(role);
        transaction.commit();
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(role));
    }

    @AfterEach
    void tearDown() {
        session.close();
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(user, actual);
    }

    @Test
    void save_UserIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> {
            userDao.save(null);
        });
    }

    @Test
    void findByEmail_Ok() {
        transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        Optional<User> optionalUserByEmail = userDao.findByEmail(email);
        assertTrue(optionalUserByEmail.isPresent());
        assertEquals(user, optionalUserByEmail.get());
    }

    @Test
    void findByEmail_EmailIsNull_Ok() {
        Optional<User> actual = userDao.findByEmail(null);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    void findByEmail_InvalidEmail_Ok() {
        Optional<User> actual = userDao.findByEmail("user");
        assertEquals(Optional.empty(), actual);
    }

    @Test
    void findById_Ok() {
        transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        Optional<User> optionalUserByEmail = userDao.findById(1L);
        assertTrue(optionalUserByEmail.isPresent());
        assertEquals(user, optionalUserByEmail.get());
    }

    @Test
    void findById_IdIsNull_Ok() {
        assertThrows(DataProcessingException.class, () -> {
            userDao.findById(null);
        });
    }

    @Test
    void findById_InvalidId_Ok() {
        Optional<User> actual = userDao.findById(2L);
        assertEquals(Optional.empty(), actual);
    }
}
