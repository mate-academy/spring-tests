package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        Role roleADMIN = new Role(Role.RoleName.ADMIN);
        Role actual = roleDao.save(roleADMIN);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(roleADMIN,actual);
    }

    @Test
    void getRoleByName_DataProcessingException() {
        try {
            roleDao.getRoleByName("USER_TEST");
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: USER_TEST", e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] { Role.class };
    }
}
