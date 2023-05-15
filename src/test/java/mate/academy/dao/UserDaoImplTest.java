package mate.academy.dao;

import java.util.NoSuchElementException;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setup() {
        user = new User();
        user.setEmail("test@test.com");
        user.setPassword("1234");
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void save_notOk() {
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        UserDao userDao1 = new UserDaoImpl(sessionFactory);
        Assertions.assertThrows(DataProcessingException.class, () ->
                userDao1.save(user)
        );
    }

    @Test
    void findById_ok() {
        User expected = userDao.save(user);
        Long id = expected.getId();
        User actual = userDao.findById(id).get();
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    void findById_notOk() {
        User expected = userDao.save(user);
        Long id = expected.getId();
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        UserDao userDao1 = new UserDaoImpl(sessionFactory);
        Assertions.assertThrows(DataProcessingException.class, () ->
                userDao1.findById(id)
        );
    }

    @Test
    void findByEmail_ok() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = roleDao.save(role);
        Set<Role> roles = Set.of(role);
        user.setRoles(roles);
        User expected = userDao.save(user);
        User actual = userDao.findByEmail(user.getEmail()).get();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findByEmail_sessionIsAbsent_notOk() {
        SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
        UserDao userDao1 = new UserDaoImpl(sessionFactory);
        Assertions.assertThrows(DataProcessingException.class, () ->
                userDao1.findByEmail(null).get()
        );
    }

    @Test
    void findByEmail_invalidEmail_notOk() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                userDao.findByEmail("alice@test.com").get()
        );
    }
}
