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
    private Role userRole;
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        userRole = new Role(Role.RoleName.USER);
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(userRole)).thenReturn(userRole);
        Role actual = roleService.save(userRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userRole.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(Optional.ofNullable(userRole));
        Role actual = roleService.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userRole.getRoleName(), actual.getRoleName());
    }
}
