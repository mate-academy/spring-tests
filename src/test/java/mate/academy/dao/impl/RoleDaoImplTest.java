package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

class RoleDaoImplTest extends AbstractDaoTest {
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
        Role expected = new Role();
        expected.setRoleName(Role.RoleName.USER);
        Role actualRole = roleDao.save(expected);
        Assertions.assertNotNull(actualRole);
        Assertions.assertEquals(1L, actualRole.getId());
        Assertions.assertEquals(expected.getRoleName(), actualRole.getRoleName());
    }

    @Test
    void get_byName_ok() {
        Role expected = new Role();
        expected.setRoleName(Role.RoleName.USER);
        roleDao.save(expected);
        Optional<Role> actualRoleOptional = roleDao.getRoleByName(expected.getRoleName().name());
        Assertions.assertTrue(actualRoleOptional.isPresent());
        Role actualRole = actualRoleOptional.get();
        Assertions.assertEquals(expected.getId(), actualRole.getId());
        Assertions.assertEquals(expected.getRoleName(), actualRole.getRoleName());
    }

    @Test
    void get_byNotExistentName_NotOk() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);
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
