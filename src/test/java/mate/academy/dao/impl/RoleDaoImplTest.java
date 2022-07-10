package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class, Role.RoleName.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Role saveRole = roleDao.save(role);
        Optional<Role> actualOptional = roleDao.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actualOptional.get());
        Assertions.assertEquals(saveRole.getRoleName(), actualOptional.get().getRoleName());
    }
}
