package mate.academy.dao.impl;

import mate.academy.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static RoleDao roleDao;

    @BeforeEach
    public void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    public void getRoleByName_ok() {
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        Role expected = roleDao.save(userRole);
        Role actual = roleDao.getRoleByName("USER").orElseGet(
                () -> Assertions.fail("Role Optional should not be empty"));
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName());
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void getRoleByName_notExistingRoleName_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("BOHDAN"));
    }

    @Test
    public void getRoleByName_nullRoleName_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}
