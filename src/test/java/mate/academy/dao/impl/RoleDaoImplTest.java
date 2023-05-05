package mate.academy.dao.impl;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDaoImpl roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_successSave_ok() {
        Role expected = new Role(Role.RoleName.ADMIN);
        Role actual = roleDao.save(expected);
        assertNotNull(actual, "There was no one role added to DB");
        assertEquals(1L, actual.getId(), "ID doesn't equal");
    }

    @Test
    void save_nullValue_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_successGetRole_ok() {
        Role expected = new Role(Role.RoleName.USER);
        roleDao.save(expected);
        Optional<Role> role = roleDao.getRoleByName("USER");
        if (role.isEmpty()) {
            Assertions.fail("DB should has role with name " + expected.getRoleName());
        }
        Assertions.assertEquals(expected.getRoleName(), role.get().getRoleName(), "Roles don't equal");
    }

    @Test
    void getRoleByName_lowerCaseRole_notOk() {
        Role expected = new Role(Role.RoleName.USER);
        roleDao.save(expected);
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName("user"),
                "Method should throw DataProcessingException for the value user");
    }

    @Test
    void getRoleByName_nullValue_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName(null),
                "Method should throw DataProcessingException for null value");
    }

    @Test
    void getRoleByName_noRolePresentInDB_notOk() {
        Optional<Role> role = roleDao.getRoleByName("USER");
        Assertions.assertEquals(role, Optional.empty(), "Method should return Optional.empty()");
    }

    @Test
    void getRoleByName_noPresentRoleNameEnum_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName("MANAGER"),
                "Method should throw DataProcessingException when no present RoleName");
    }

    @Test
    void findAll_successFind_ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        roleDao.save(role);
        roleDao.save(role);
        List<Role> allRoles = roleDao.findAll();
        assertNotNull(allRoles, "List of roles should be not null");
        assertEquals(allRoles.size(), 3, "List has to contain 3 element, but actual " + allRoles.size());
    }

    @Test
    void findAll_noOneRolesPresentInDB_notOk() {
        List<Role> allRoles = roleDao.findAll();
        assertNotNull(allRoles, "List of roles should be not null");
        assertTrue(allRoles.isEmpty(), "List is not empty");
    }

    @Test
    void findById_successFindById_ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Optional<Role> roleById = roleDao.findById(1L);
        if (roleById.isEmpty()) {
            Assertions.fail("No one role present in DB. You have some problem with save() method");
        }
        Assertions.assertEquals(roleById.get().getId(), 1L,
                "IDs don't equal. Actual id = " + roleById.get().getId() + " but must be 1");
    }

    @Test
    void findById_noRolePresentInDbById_notOk() {
        Optional<Role> roleById = roleDao.findById(1L);
        Assertions.assertEquals(roleById, Optional.empty(), "Method should return Optional.empty()");
    }

    @Test
    void findById_nullValue_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.findById(null),
                "Method should throw DataProcessingException for null value");
    }

    @Test
    void delete_successDeleteRole_ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        roleDao.save(role);
        roleDao.save(role);
        roleDao.delete(1L);
        List<Role> allRoles = roleDao.findAll();
        for(Role roleFromDB : allRoles) {
            if (roleFromDB.getId().equals(1L)) {
                Assertions.fail("You don't delete role by id 1, or delete the wrong one");
            }
        }
        Assertions.assertEquals(allRoles.size(), 2,
                "A size of all roles should be 2, but actual " + allRoles.size());
    }

    @Test
    void delete_noRolesToDelete_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.delete(1L),
                "Method should throw DataProcessingException when no one role in DB with id 1");
    }

    @Test
    void delete_nullValue_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.delete(null),
                "Method should throw DataProcessingException for null value");
    }

    @Test
    void update_successUpdateRole_ok() {
        Role saved = new Role(Role.RoleName.USER);
        roleDao.save(saved);

        saved.setRoleName(Role.RoleName.ADMIN);
        Role updated = roleDao.update(saved);
        Assertions.assertNotNull(updated, "Updated role should be no null");
        Assertions.assertEquals(saved.getId(), updated.getId(), "Id doesn't equals");
        Assertions.assertEquals(updated.getRoleName(), Role.RoleName.ADMIN,
                "RoleName doesn't equals");
    }

    @Test
    void update_noRoleToUpdate_notOk() {
        Role role = new Role(Role.RoleName.USER);
        Assertions.assertThrows(DataProcessingException.class, () ->roleDao.update(role),
                "Method should throw DataProcessingException for transient object");
    }

    @Test
    void update_nullValue_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleDao.update(null),
                "Method should throw DataProcessingException for null value");
    }
}