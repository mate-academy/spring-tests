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
    private RoleDao roleDao;
    private RoleService roleService;
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
        Mockito.when(roleDao.save(Mockito.any())).thenReturn(expected);
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Role actual = roleService.save(role);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getRoleByName_validName_ok() {
        Mockito.when(roleDao.getRoleByName("USER")).thenReturn(Optional.of(expected));
        Assertions.assertEquals(expected, roleService.getRoleByName("USER"));
    }

    @Test
    void getRoleByName_invalidName_notOk() {
        Mockito.when(roleDao.getRoleByName("SOME_ROLE")).thenThrow();
        Assertions.assertThrows(Exception.class, () -> {roleService.getRoleByName("SOME_ROLE");});
    }
}