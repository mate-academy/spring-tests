package mate.academy.dao;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoleDaoImplTest extends AbstractTest {
    private RoleDaoImpl roleDaoImpl;
    private Role roleAdmin;
    private Role roleUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDaoImpl = new RoleDaoImpl(this.getSessionFactory());
        roleAdmin = new Role(Role.RoleName.ADMIN);
        roleUser = new Role(Role.RoleName.USER);
    }

    @Test
    void save_roleAdmin_ok() {
        Role roleActual = roleDaoImpl.save(roleAdmin);
        assertNotNull(roleActual);
        assertEquals(1L, roleActual.getId());
    }

    @Test
    void save_null_notOk() {
        try {
            roleDaoImpl.save(null);
        } catch (DataProcessingException e) {
            assertEquals("Can't create entity: null", e.getMessage());
        }
    }

    @Test
    void findAll_withTwoEntities_ok() {
        roleDaoImpl.save(roleAdmin);
        roleDaoImpl.save(roleUser);
        assertEquals(2, roleDaoImpl.findAll().size());
    }

    @Test
    void findById_correctId_ok() {
        roleDaoImpl.save(roleAdmin);
        Optional<Role> actualOptional = roleDaoImpl.findById(1L);
        assertNotNull(actualOptional.get());
        assertEquals(roleAdmin.getRoleName(), actualOptional.get().getRoleName());
    }

    @Test
    void findById_incorrectId_notOk() {
        roleDaoImpl.save(roleAdmin);
        assertEquals(Optional.empty(), roleDaoImpl.findById(2L));
    }

    @Test
    void delete_correctId_ok() {
        roleDaoImpl.save(roleAdmin);
        Role savedUserRole = roleDaoImpl.save(roleUser);
        roleDaoImpl.delete(savedUserRole.getId());
        List<Role> allRoles = roleDaoImpl.findAll();
        assertEquals(1, allRoles.size());
        assertEquals(allRoles.get(0).getRoleName(), roleAdmin.getRoleName());
    }

    @Test
    void delete_incorrectId_notOk() {
        roleDaoImpl.save(roleAdmin);
        try {
            roleDaoImpl.delete(2L);
        } catch (DataProcessingException e) {
            assertEquals("Can't delete entity by id: 2", e.getMessage());
        }
    }

    @Test
    void update_correctData_ok() {
        Role savedRole = roleDaoImpl.save(roleAdmin);
        savedRole.setRoleName(Role.RoleName.USER);
        roleDaoImpl.update(savedRole);
        assertEquals(1L, savedRole.getId());
        assertNotEquals(Role.RoleName.ADMIN, savedRole.getRoleName());
        assertEquals(Role.RoleName.USER, savedRole.getRoleName());
    }

    @Test
    void update_incorrectId_notOk() {
        roleDaoImpl.save(roleAdmin);
        try {
            roleDaoImpl.update(roleUser);
        } catch (DataProcessingException e) {
            assertEquals("Can't update entity: " + roleUser, e.getMessage());
        }
    }

    @Test
    void getRoleByName_correctData_ok() {
        roleDaoImpl.save(roleAdmin);
        roleDaoImpl.save(roleUser);
        Optional<Role> roleUserOptional = roleDaoImpl.getRoleByName("USER");
        assertNotNull(roleUserOptional.get());
        assertEquals(roleUser.getRoleName(), roleUserOptional.get().getRoleName());
        Optional<Role> roleAdminOptional = roleDaoImpl.getRoleByName("ADMIN");
        assertNotNull(roleAdminOptional.get());
        assertEquals(roleAdmin.getRoleName(), roleAdminOptional.get().getRoleName());
    }

    @Test
    void getRoleByName_incorrectId_notOk() {
        roleDaoImpl.save(roleAdmin);
        roleDaoImpl.save(roleUser);
        try {
            roleDaoImpl.getRoleByName("MANAGER");
        } catch (DataProcessingException e) {
            assertEquals("Couldn't get role by role name: MANAGER", e.getMessage());
        }

    }
}