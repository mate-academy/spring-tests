package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void saveRole_ok() {
        Role actual = roleDao.save(role);
        Assertions.assertEquals(actual, role);
    }

    @Test
    void getRoleByName_ok() {
        roleDao.save(role);
        Optional<Role> roleOptional = roleDao.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertTrue(roleOptional.isPresent());
        Assertions.assertEquals(role.getRoleName(), roleOptional.get().getRoleName());
    }

    @Test
    void getRoleByIncorrectName_notOk() {
        Assertions.assertTrue(roleDao.getRoleByName(Role.RoleName.USER.name()).isEmpty());
    }
}
