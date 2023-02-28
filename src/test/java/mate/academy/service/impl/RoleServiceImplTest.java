package mate.academy.service.impl;

import java.util.NoSuchElementException;
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

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void getRoleByName_validRoleName_shouldReturnRole() {
        String roleName = "USER";
        Role expected = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.getRoleByName(roleName))
                .thenReturn(Optional.of(expected));
        Role actual = roleService.getRoleByName(roleName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getRoleByName_unValidRoleName_shouldThrowNoSuchElementException() {
        String unValidRoleName = "PLAYER";
        Mockito.when(roleDao.getRoleByName(unValidRoleName)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(unValidRoleName));
    }
}
