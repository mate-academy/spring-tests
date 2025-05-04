package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static RoleService roleService;
    private static RoleDao roleDao;

    @BeforeAll
    static void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void getRoleByName_ok() {
        String expected = "USER";
        Role userRole = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.getRoleByName(expected)).thenReturn(Optional.of(userRole));
        Role actual = roleService.getRoleByName(expected);
        Assertions.assertEquals(expected, actual.getRoleName().name());
    }

    @Test
    void getRoleByName_nullName_notOk() {
        Mockito.when(roleDao.getRoleByName(null)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () ->
                roleService.getRoleByName(null));
    }

    @Test
    void getRoleByName_emptyName_notOk() {
        Mockito.when(roleDao.getRoleByName(null)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName(""));
    }
}
