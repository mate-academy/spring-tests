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
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void saveRole_ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_ok() {
        roleDao.save(role);
        Optional<Role> optionalRole = roleDao.getRoleByName(role.getRoleName().name());
        Assertions.assertTrue(optionalRole.isPresent());
        Assertions.assertEquals(role.getRoleName().name(),
                optionalRole.get().getRoleName().name());
    }

    @Test
    void getRoleByName_NotExistName_Empty() {
        Optional<Role> optionalRole = roleDao.getRoleByName(role.getRoleName().name());
        Assertions.assertTrue(optionalRole.isEmpty());
    }
}
