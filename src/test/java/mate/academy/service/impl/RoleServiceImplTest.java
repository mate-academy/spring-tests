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
    public static final String USER_ROLE = "USER";
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
    void save_ok() {
        Mockito.when(roleService.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(USER_ROLE)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(USER_ROLE);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getRoleName(), Role.RoleName.USER);
    }
}