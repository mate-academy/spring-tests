package mate.academy.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private final static String EMAIL = "bob@gmail.com";
    private final static String PASSWORD = "1234";
    private UserDaoImpl userDao;
    private RoleDao roleDao;
    private Role userRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = roleDao.save(new Role(Role.RoleName.USER));
    }

    @Test
    void save_successSave_ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));

        User saved = userDao.save(user);
        Assertions.assertNotNull(saved, "There was no one user added to DB");
        Assertions.assertEquals(1L, saved.getId(), "ID doesn't equal");
        Assertions.assertEquals(EMAIL, saved.getEmail(), "Email doesn't equal");
        Assertions.assertEquals(PASSWORD, saved.getPassword(), "Password doesn't equal");
        Assertions.assertTrue(saved.getRoles().contains(userRole), "Role doesn't contain");
    }

    @Test
    void save_emailPresentInDb_notOk() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
        userDao.save(user);

        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(user),
                "Method should throw DataProcessingException when user with email "
                        + EMAIL + " exists in DB");
    }

    @Test
    void save_nullValue_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(null),
                "Method should throw DataProcessingException for null value");
    }

    @Test
    void findByEmail_successFind_ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
        userDao.save(user);

        Optional<User> userByEmail = userDao.findByEmail(EMAIL);
        if (userByEmail.isEmpty()) {
            Assertions.fail("DB should has role with name " + user.getEmail());
        }
        Assertions.assertEquals(1L, user.getId(), "ID doesn't equal");
        Assertions.assertEquals(EMAIL, user.getEmail(), "Email doesn't equal");
        Assertions.assertEquals(PASSWORD, user.getPassword(), "Password doesn't equal");
        Assertions.assertTrue(user.getRoles().contains(userRole), "Role doesn't contain");
    }

    @Test
    void findByEmail_nullValue_notOk() {
        Optional<User> userByEmail = userDao.findByEmail(null);
        Assertions.assertEquals(userByEmail, Optional.empty(), "Method should return Optional.empty()");
    }

    @Test
    void findByEmail_noRolePresentInDB_notOk() {
        Optional<User> userByEmail = userDao.findByEmail("alice@gmail.com");
        Assertions.assertEquals(userByEmail, Optional.empty(), "Method should return Optional.empty()");
    }

    @Test
    void findAll_successFind_ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
        userDao.save(user);
        user.setEmail("alice@gmail.com");
        userDao.save(user);
        user.setEmail("tom@gmail.com");
        userDao.save(user);

        List<User> allRoles = userDao.findAll();
        assertNotNull(allRoles, "List of roles should be not null");
        assertEquals(allRoles.size(), 3, "List has to contain 3 element, but actual " + allRoles.size());
    }

    @Test
    void findAll_noOneRolesPresentInDB_notOk() {
        List<User> allRoles = userDao.findAll();
        assertNotNull(allRoles, "List of roles should be not null");
        assertTrue(allRoles.isEmpty(), "List is not empty");
    }

    @Test
    void findById_successFindById_ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
        userDao.save(user);
        Optional<User> userById = userDao.findById(1L);
        if (userById.isEmpty()) {
            Assertions.fail("No one user present in DB. You have some problem with save() method");
        }
        Assertions.assertEquals(userById.get().getId(), 1L,
                "IDs don't equal. Actual id = " + userById.get().getId() + " but must be 1");
    }

    @Test
    void findById_noUserPresentInDbById_notOk() {
        Optional<User> userById = userDao.findById(1L);
        Assertions.assertEquals(userById, Optional.empty(), "Method should return Optional.empty()");
    }

    @Test
    void findById_nullValue_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.findById(null),
                "Method should throw DataProcessingException for null value");
    }

    @Test
    void delete_successDeleteRole_ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
        userDao.save(user);
        user.setEmail("alice@gmail.com");
        userDao.save(user);
        user.setEmail("tom@gmail.com");
        userDao.save(user);
        userDao.delete(1L);
        List<User> users = userDao.findAll();
        for(User userFromDB : users) {
            if (userFromDB.getId().equals(1L)) {
                Assertions.fail("You don't delete user by id 1, or delete the wrong one");
            }
        }
        Assertions.assertEquals(users.size(), 2,
                "A size of all users should be 2, but actual " + users.size());
    }

    @Test
    void delete_noRolesToDelete_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.delete(1L),
                "Method should throw DataProcessingException when no one user in DB with id 1");
    }

    @Test
    void delete_nullValue_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.delete(null),
                "Method should throw DataProcessingException for null value");
    }

    @Test
    void update_successUpdateUser_ok() {
        User saved = new User();
        saved.setEmail(EMAIL);
        saved.setPassword(PASSWORD);
        saved.setRoles(Set.of(userRole));
        userDao.save(saved);

        saved.setEmail("alice@gmail.com");
        saved.setPassword("4321");
        User updated = userDao.update(saved);
        Assertions.assertNotNull(updated, "Updated saved should be no null");
        Assertions.assertEquals(saved.getId(), updated.getId(), "Id was changed");
        Assertions.assertEquals(updated.getEmail(), "alice@gmail.com", "Email don't matches");
        Assertions.assertEquals(updated.getPassword(), "4321", "Password don't matches");
        Assertions.assertEquals(saved.getRoles(), updated.getRoles(), "Roles were changed");
    }

    @Test
    void update_noUserToUpdate_notOk() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
        Assertions.assertThrows(DataProcessingException.class, () ->userDao.update(user),
                "Method should throw DataProcessingException for transient object");
    }

    @Test
    void update_nullValue_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.update(null),
                "Method should throw DataProcessingException for null value");
    }
}