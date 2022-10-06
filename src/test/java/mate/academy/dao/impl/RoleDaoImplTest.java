package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractClass {
    private static final String UNEXISTING_ROLE = "GUEST";
    private RoleDao roleDao;
    private Role roleName = new Role();

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleName.setRoleName(Role.RoleName.ADMIN);
    }

    @Test
    void save_ToDb_Ok() {
        Role actual = roleDao.save(roleName);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(Role.RoleName.ADMIN, actual.getRoleName());
    }

    @Test
    void getRoleByName_RoleExist_Ok() {
        roleDao.save(roleName);
        Optional<Role> actual = roleDao.getRoleByName(Role.RoleName.ADMIN.name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get().getRoleName(), Role.RoleName.ADMIN);
    }

    @Test
    void getRoleByName_RoleDoesNotExist_NotOk() {
        roleDao.save(roleName);
        Assertions.assertThrows(DataProcessingException.class, () ->
                        roleDao.getRoleByName(UNEXISTING_ROLE),
                "Expected to receive DataProcessingException.");
    }
}
