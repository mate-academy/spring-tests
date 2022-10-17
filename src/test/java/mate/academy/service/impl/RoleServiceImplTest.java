package mate.academy.service.impl;

import java.util.NoSuchElementException;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleServiceImplTest extends AbstractTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private Role userRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        userRole = new Role(Role.RoleName.USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Role actual = roleService.save(userRole);
        Assertions.assertEquals(userRole.getRoleName(), actual.getRoleName());
        Assertions.assertNotNull(actual);
    }

    @Test
    void getRoleByName_Ok() {
        roleService.save(userRole);
        Role actual = roleService.getRoleByName("USER");
        Assertions.assertEquals(userRole.getRoleName(), actual.getRoleName());
        Assertions.assertNotNull(actual);
    }

    @Test
    void getRoleByName_NotOk() {
        roleService.save(userRole);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName("ADMIN"));
    }
}
