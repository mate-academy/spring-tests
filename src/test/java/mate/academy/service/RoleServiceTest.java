package mate.academy.service;

import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.NoSuchElementException;
import java.util.Optional;

class RoleServiceTest {
    private static RoleService roleService;
    private static RoleDao roleDao;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDaoImpl.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Long identifier = 1L;
        Role role = new Role(Role.RoleName.USER);
        role.setId(identifier);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual, "Role must not be null for: " + role);

    }

    @Test
    void getRoleByName_Ok() {
        Long identifier = 1L;
        String correctRoleName = Role.RoleName.USER.name();
        Role role = new Role(Role.RoleName.USER);
        role.setId(identifier);
        Mockito.when(roleDao.getRoleByName(correctRoleName)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(correctRoleName);
        Assertions.assertNotNull(actual, "Role must not be null for role name: " + correctRoleName);
    }

    @Test
    void getRoleByName_nonExistentRole_NotOk() {
        Long identifier = 1L;
        String incorrectRoleName = "SASHA";
        Role role = new Role(Role.RoleName.USER);
        role.setId(identifier);
        Mockito.when(roleDao.getRoleByName(incorrectRoleName)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> {roleService.getRoleByName(incorrectRoleName);},
                "NoSuchElementException expected for role name: " + incorrectRoleName);
    }
}
