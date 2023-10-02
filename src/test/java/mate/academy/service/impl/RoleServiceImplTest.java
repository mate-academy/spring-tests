package mate.academy.service.impl;

import mate.academy.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleServiceImplTest extends AbstractTest {
    private RoleService roleService;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleService = new RoleServiceImpl(roleDao);
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("USER", actual.getRoleName().name());
    }

    @Test
    void getRoleByName_Ok() {
        roleService.save(role);
        Role actual = roleService.getRoleByName("USER");
        Assertions.assertEquals("USER", actual.getRoleName().name());
        Assertions.assertEquals(1L, actual.getId());
    }
}