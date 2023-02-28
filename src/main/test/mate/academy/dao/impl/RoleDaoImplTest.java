package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
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
    void save_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Role actual = roleDao.save(role);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Role actual = roleDao.save(role);
        Optional<Role> optionalRole = roleDao.getRoleByName("USER");
        Assertions.assertTrue(optionalRole.isPresent());
        Assertions.assertEquals(role, optionalRole.get());
    }

    @Test
    void getRoleByName_NoRole() {
        Optional<Role> optionalRole = roleDao.getRoleByName("USER");
        Assertions.assertEquals(Optional.empty(), optionalRole);
    }
}
