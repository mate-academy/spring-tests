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
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void getRoleByName_ok() {
        String roleName = "USER";
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(roleName);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName(),
                "Method should return role: " + role.getRoleName().name()
                        + " but was: " + actual.get().getRoleName().name());
    }

    @Test
    void getRoleByName_notOk() {
        String roleName = "LORD";
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(roleName),
                "Method should throw: " + DataProcessingException.class
                        + " for non existing role: " + roleName);
    }
}
