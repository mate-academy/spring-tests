package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String USERNAME = "bob@i.ua";
    private static final Long FIRST_ID = 1L;
    private UserDao userDao;
    private User user;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(USERNAME);
        role = new Role(Role.RoleName.USER);
        RoleDaoImpl roleDao = new RoleDaoImpl(getSessionFactory());
        role = roleDao.save(role);
        user.setRoles(Set.of(role));
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        User savedUser = userDao.save(user);
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(FIRST_ID, savedUser.getId());
        Assertions.assertEquals(USERNAME, savedUser.getEmail());
        Assertions.assertEquals(Set.of(role), savedUser.getRoles());
    }

    @Test
    void save_notUniqueEmail_notOk() {
        userDao.save(user);
        User userNotUniq = new User();
        userNotUniq.setEmail(USERNAME);
        DataProcessingException thrown =
                Assertions.assertThrows(DataProcessingException.class,
                        () -> userDao.save(userNotUniq),
                "Expected to receive DataProcessingException");
        Assertions.assertEquals("Can't create entity: " + userNotUniq, thrown.getMessage());
    }

    @Test
    void findByEmail_ok() {
        userDao.save(user);
        Optional<User> optionalUser = userDao.findByEmail(USERNAME);
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals(USERNAME, optionalUser.get().getEmail());
    }

    @Test
    void findByEmail_notFind() {
        Optional<User> optionalUser = userDao.findByEmail(USERNAME);
        Assertions.assertTrue(optionalUser.isEmpty());
    }
}
