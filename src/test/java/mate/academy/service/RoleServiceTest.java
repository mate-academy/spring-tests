package mate.academy.service;

import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleServiceTest extends AbstractTest {
    private static RoleDao roleDao;
    private static RoleService roleService;

    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setup() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save() {
        Role actual = roleService.save(new Role(Role.RoleName.USER));
        Assertions.assertNotNull(actual);
    }

    @Test
    void getRoleByName() {
        roleService.save(new Role(Role.RoleName.USER));
        Role actual = roleService.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertNotNull(actual);
    }
}
