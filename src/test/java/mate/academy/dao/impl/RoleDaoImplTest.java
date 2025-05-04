package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final String ROLE_USER = "USER";
    private static final String ROLE_GOD = "GOD";
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] { Role.class };
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_existingRoleName_ok() {
        roleDao.save(role);
        Role actual = roleDao.getRoleByName(ROLE_USER).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByName_notExistingRoleName_dataProcessingException() {
        roleDao.save(role);
        Assertions.assertThrows(DataProcessingException.class, 
                () -> roleDao.getRoleByName(ROLE_GOD));
    }
}
