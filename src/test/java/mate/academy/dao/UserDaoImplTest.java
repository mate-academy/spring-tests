package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private RoleDaoImpl roleDaoImpl;
    private UserDaoImpl userDaoImpl;
    private Role roleAdmin;
    private Role roleUser;
    private User userMax;
    private User userMarina;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDaoImpl = new RoleDaoImpl(this.getSessionFactory());
        userDaoImpl = new UserDaoImpl(this.getSessionFactory());
        roleAdmin = new Role(Role.RoleName.ADMIN);
        roleUser = new Role(Role.RoleName.USER);
        roleDaoImpl.save(roleAdmin);
        roleDaoImpl.save(roleUser);
        userMax = new User();
        userMax.setEmail("max@max.com");
        userMax.setPassword("maxMax1234");
        userMax.setRoles(Set.of(roleUser));
        userMarina = new User();
        userMarina.setEmail("marina@marina.com");
        userMarina.setPassword("marinaMarina1234");
        userMarina.setRoles(Set.of(roleAdmin));

    }

    @Test
    void save_correctUser_ok() {
        User userActual = userDaoImpl.save(userMax);
        assertNotNull(userActual);
        assertEquals(1L, userActual.getId());
    }

    @Test
    void save_null_notOk() {
        try {
            userDaoImpl.save(null);
        } catch (DataProcessingException e) {
            assertEquals("Can't create entity: null", e.getMessage());
        }
    }

    @Test
    void findAll_withTwoEntities_ok() {
        userDaoImpl.save(userMax);
        userDaoImpl.save(userMarina);
        assertEquals(2, userDaoImpl.findAll().size());
    }

    @Test
    void findById_correctId_ok() {
        userDaoImpl.save(userMax);
        Optional<User> actualOptional = userDaoImpl.findById(1L);
        assertNotNull(actualOptional);
        assertEquals(userMax.getEmail(), actualOptional.get().getEmail());
    }

    @Test
    void findById_incorrectId_notOk() {
        userDaoImpl.save(userMax);
        assertEquals(Optional.empty(), userDaoImpl.findById(2L));
    }

    @Test
    void delete_correctId_ok() {
        userDaoImpl.save(userMax);
        User savedUser = userDaoImpl.save(userMarina);
        userDaoImpl.delete(savedUser.getId());
        List<User> allUsers = userDaoImpl.findAll();
        assertEquals(1, allUsers.size());
        assertEquals(allUsers.get(0).getEmail(), userMax.getEmail());
    }

    @Test
    void delete_incorrectId_notOk() {
        userDaoImpl.save(userMax);
        try {
            userDaoImpl.delete(2L);
        } catch (DataProcessingException e) {
            assertEquals("Can't delete entity by id: 2", e.getMessage());
        }
    }

    @Test
    void update_correctData_ok() {
        User savedUser = userDaoImpl.save(userMax);
        savedUser.setRoles(Set.of(roleUser, roleAdmin));
        userDaoImpl.update(savedUser);
        assertEquals(1L, savedUser.getId());
        assertNotEquals(1, savedUser.getRoles().size());
        assertEquals(2, savedUser.getRoles().size());
        assertTrue(savedUser.getRoles().contains(roleUser)
                && savedUser.getRoles().contains(roleAdmin));
    }

    @Test
    void update_incorrectId_notOk() {
        userDaoImpl.save(userMax);
        try {
            userDaoImpl.update(userMarina);
        } catch (DataProcessingException e) {
            assertEquals("Can't update entity: " + userMarina, e.getMessage());
        }
    }

    @Test
    void findByEmail_correctData_ok() {
        userDaoImpl.save(userMax);
        userDaoImpl.save(userMarina);
        Optional<User> userMaxOptional = userDaoImpl.findByEmail(userMax.getEmail());
        assertNotNull(userMaxOptional);
        assertEquals(userMax.getEmail(), userMaxOptional.get().getEmail());
        Optional<User> userMarinaOptional = userDaoImpl.findByEmail(userMarina.getEmail());
        assertNotNull(userMarinaOptional);
        assertEquals(userMarina.getEmail(), userMarinaOptional.get().getEmail());
    }

    @Test
    void findByEmail_incorrectId_notOk() {
        userDaoImpl.save(userMax);
        userDaoImpl.save(userMarina);
        assertEquals(Optional.empty(), userDaoImpl.findByEmail("test@test.com"));

    }
}
