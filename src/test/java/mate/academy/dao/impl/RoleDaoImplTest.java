package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
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
    void save_Ok() {
        Role role = new Role(Role.RoleName.USER);
        Role actual = roleDao.save(role);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(actual.getRoleName(), Role.RoleName.USER);
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.USER.name());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get().getRoleName(), Role.RoleName.USER);
    }

    @Test
    void getRoleByName_NotOk_DataProcessingException() {
        roleDao = new RoleDaoImpl(null);
        String roleName = Role.RoleName.ADMIN.name();

        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(roleName));
    }
}
