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
    private Role expected;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        expected = new Role();
        expected.setRoleName(Role.RoleName.USER);
        expected.setId(1L);
    }

    @Test
    void save_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);

        Mockito.when(roleDao.save(Mockito.any())).thenReturn(expected);
        Role actual = roleService.save(role);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(Mockito.any())).thenReturn(Optional.of(expected));
        Role actual = roleService.getRoleByName("User");
        Assertions.assertEquals(expected, actual);
    }
}
