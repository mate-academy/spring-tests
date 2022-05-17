package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_validRole_ok() {
        Role expectedRole = new Role();
        expectedRole.setRoleName(Role.RoleName.USER);
        Role actualRole = roleDao.save(expectedRole);
        Assertions.assertNotNull(actualRole);
        Assertions.assertEquals(1L, actualRole.getId());
        Assertions.assertEquals(expectedRole.getRoleName(), actualRole.getRoleName());
    }

    @Test
    void save_existingRole_notOk() {
        Role roleA = new Role();
        roleA.setRoleName(Role.RoleName.USER);
        Role roleB = new Role();
        roleB.setRoleName(Role.RoleName.USER);
        Role actualRoleA = roleDao.save(roleA);
        Assertions.assertNotNull(actualRoleA);
        try {
            roleDao.save(roleB);
        } catch (Exception e) {
            Class<? extends Exception> expected = DataProcessingException.class;
            Class<? extends Exception> actual = e.getClass();
            Assertions.assertEquals(expected, actual);
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void getRoleByName_ok() {
        Role expectedRole = new Role();
        expectedRole.setRoleName(Role.RoleName.USER);
        roleDao.save(expectedRole);
        Optional<Role> actualRoleOptional = roleDao.getRoleByName(expectedRole.getRoleName().name());
        Assertions.assertTrue(actualRoleOptional.isPresent());
        Role actualRole = actualRoleOptional.get();
        Assertions.assertEquals(expectedRole.getId(), actualRole.getId());
        Assertions.assertEquals(expectedRole.getRoleName(), actualRole.getRoleName());
    }

    @Test
    void getRoleByName_Exception() {
        Role expectedRole = new Role();
        expectedRole.setRoleName(Role.RoleName.USER);
        roleDao.save(expectedRole);
        String unknownRoleName = "unknown";
        try {
            roleDao.getRoleByName(unknownRoleName);
        } catch (Exception e) {
            Class<? extends Exception> expected = DataProcessingException.class;
            Class<? extends Exception> actual = e.getClass();
            Assertions.assertEquals(expected, actual);
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}