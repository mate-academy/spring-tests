package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleService.save(userRole)).thenReturn(userRole);
        Role actual = roleService.save(userRole);
        Assertions.assertEquals(userRole.getRoleName(), actual.getRoleName());
        Assertions.assertNotNull(actual);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName("USER")).thenReturn(Optional.ofNullable(userRole));
        Role actual = roleService.getRoleByName("USER");
        Assertions.assertEquals(userRole.getRoleName(), actual.getRoleName());
        Assertions.assertNotNull(actual);
    }
}
