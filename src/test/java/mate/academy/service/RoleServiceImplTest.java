package mate.academy.service;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RoleServiceImplTest {
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName("USER"))
                .thenReturn(Optional.of(new Role(Role.RoleName.USER)));
        Role.RoleName expected = Role.RoleName.USER;
        Role.RoleName actual = roleService.getRoleByName("USER").getRoleName();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getRoleByName_notOk() {
        Mockito.when(roleDao.getRoleByName("USER"))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class,
                () -> roleService.getRoleByName("USER"));
    }

    @Test
    void save_ok() {
        Role role = new Role(Role.RoleName.USER);
        role.setId(1L);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role.RoleName expected = Role.RoleName.USER;
        Role.RoleName actual = roleService.save(role).getRoleName();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void save_notOk() {
        Role role = new Role(Role.RoleName.USER);
        role.setId(1L);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role.RoleName expected = Role.RoleName.ADMIN;
        Role.RoleName actual = roleService.save(role).getRoleName();
        Assertions.assertNotEquals(expected, actual);
    }
}
