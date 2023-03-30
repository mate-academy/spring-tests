package mate.academy.service.impl;

import static org.mockito.ArgumentMatchers.any;

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
    private static RoleService roleService;
    private static RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.getRoleByName(any())).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getRoleName().name(), role.getRoleName().name(),
                "Method should return: " + role + " but was: " + actual);
    }

    @Test
    void getRoleByName_NotOk() {
        String roleName = "BOT";
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(roleName),
                "Method should return: " + NoSuchElementException.class);
    }
}
