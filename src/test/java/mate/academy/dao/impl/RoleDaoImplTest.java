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
    private Role userRole;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role(Role.RoleName.USER);
        roleDao.save(userRole);
    }

    @Test
    void getRoleByName_Ok() {
        Optional<Role> optionalRole = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertTrue(optionalRole.isPresent());
        Assertions.assertEquals(userRole, optionalRole.get());
    }

    @Test
    void getRoleByName_invalidRoleName_NotOk() {
        Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName("DEVELOPER"));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}
