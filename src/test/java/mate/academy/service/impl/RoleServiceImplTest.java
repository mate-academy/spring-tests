package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_Ok() {
        String roleName = role.getRoleName().name();
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(roleName);
        Assertions.assertEquals(roleName, role.getRoleName().name());
    }
}
