package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    private Role role;
    private RoleService roleService;
    private Optional<Role> roleNull;
    @Mock
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        role.setId(1L);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        roleService = new RoleServiceImpl(roleDao);
        Role actualRole = roleService.save(role);
        Assertions.assertNotNull(actualRole);
        Assertions.assertEquals(1L, actualRole.getId());
        Assertions.assertEquals(Role.RoleName.USER, actualRole.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(Optional.ofNullable(role));
        roleService = new RoleServiceImpl(roleDao);
        Role actualRole = roleService.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertNotNull(actualRole);
        Assertions.assertEquals(1L, actualRole.getId());
        Assertions.assertEquals(Role.RoleName.USER, actualRole.getRoleName());
    }

    @Test
    void getRoleByName_NoSuchElementException() {
        Mockito.when(roleDao.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(Optional.ofNullable(null));
        roleService = new RoleServiceImpl(roleDao);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> {
                roleService.getRoleByName(Role.RoleName.USER.name());
                }, "NoSuchElementException was expected.");
    }
}
