package mate.academy.dao.service.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Role role = new Role(Role.RoleName.ADMIN);
        Mockito.when(roleDao.save(role)).thenReturn(new Role(Role.RoleName.ADMIN));
        Role actual = roleService.save(role);
        Assertions.assertEquals(actual.getRoleName(), role.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role(Role.RoleName.ADMIN);
        String roleName = Role.RoleName.ADMIN.name();
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(roleName);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }
}
