package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private Role expectedRole;
    private String roleName;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        roleName = "USER";
        expectedRole = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(expectedRole)).thenReturn(expectedRole);
        Role actualRole = roleService.save(expectedRole);
        Assertions.assertEquals(actualRole.getRoleName(), expectedRole.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.of(expectedRole));
        Role actualRole = roleService.getRoleByName(roleName);
        Assertions.assertEquals(actualRole.getRoleName().name(), roleName);
    }

    @Test
    void getRoleByName_roleNameIsNull_Ok() {
        Mockito.when(roleDao.getRoleByName(null)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(null));
    }

    @Test
    void getRoleByName_roleNameIsEmpty_Ok() {
        Mockito.when(roleDao.getRoleByName("")).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(""));
    }
}
