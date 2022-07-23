package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

class RoleServiceTest {
    private static RoleDao roleDao;
    private static RoleService roleService;

    @BeforeAll
    public static void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(2L);
        Mockito.when(roleDao.save(ArgumentMatchers.any())).thenReturn(userRole);
        Mockito.when(roleDao.save(null)).thenThrow(DataProcessingException.class);
    }

    @Test
    public void save_ok() {
        Role userRole = getDummyRole();
        Role actual = roleService.save(userRole);
        Assertions.assertEquals(Role.RoleName.USER, actual.getRoleName());
        Assertions.assertNotNull(actual.getId());
    }

    @Test
    public void save_nullInput_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleService.save(null));
    }

    @Test
    public void getRoleByName_ok() {
        Role userRole = getDummyRole();
        Mockito.when(roleDao.getRoleByName("USER")).thenReturn(Optional.of(userRole));
        Role actual = roleService.getRoleByName("USER");
        Assertions.assertEquals(Role.RoleName.USER, actual.getRoleName());
        Assertions.assertNotNull(actual.getId());
    }

    @Test
    public void getRoleByName_noSuchRole_notOk() {
        Mockito.when(roleDao.getRoleByName(ArgumentMatchers.any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName("BOHDAN"));
    }

    @Test
    public void getRoleByName_nullInput_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> roleService.getRoleByName(null));
    }

    private Role getDummyRole() {
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(2L);
        return userRole;
    }
}
