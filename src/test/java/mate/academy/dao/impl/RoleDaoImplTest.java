package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role roleUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleUser = new Role(Role.RoleName.USER);
    }

    @Test
    void saveRole_ok() {
        Role savedRole = roleDao.save(roleUser);
        assertNotNull(savedRole.getId());
        assertEquals(Role.RoleName.USER.name(), savedRole.getRoleName().name());
    }

    @Test
    void getRoleByName_ok() {
        roleDao.save(roleUser);
        Role actualRole = roleDao.getRoleByName(roleUser.getRoleName().name()).get();
        Assertions.assertNotNull(actualRole);
        Assertions.assertEquals(roleUser.getRoleName().name(), actualRole.getRoleName().name());
    }

    @Test
    void getRoleByName_wrongName_NotOk() {
        Optional<Role> actualRole = roleDao.getRoleByName(Role.RoleName.ADMIN.name());
        Assertions.assertTrue(actualRole.isEmpty());
    }
}
