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
    private RoleDao roleDao;
    private RoleService roleService;

    private Role role;
    private String roleName;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.USER);
        roleName = "USER";
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role userRoleFromDB = roleService.save(role);
        Assertions.assertEquals(userRoleFromDB.getRoleName(), role.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.of(role));
        Role roleByName = roleService.getRoleByName(roleName);
        Assertions.assertEquals(roleByName.getRoleName().name(), roleName);
    }

    @Test
    void getRoleByName_EmptyName_NotOk() {
        Mockito.when(roleDao.getRoleByName("")).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(""));
    }
}
