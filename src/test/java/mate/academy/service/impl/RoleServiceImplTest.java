package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_role_OK() {
        Mockito.when(roleDao.save(any())).thenReturn(role);
        Role actual = roleService.save(role);
        assertNotNull(actual);
        assertEquals("USER", actual.getRoleName().name());
    }

    @Test
    void getRoleByName_nullValue_notOK() {
        role.setRoleName(null);
        Mockito.when(roleDao.getRoleByName(any()))
                .thenReturn(Optional.of(role));
        assertThrows(RuntimeException.class,
                () -> roleService.getRoleByName(role.getRoleName().name()));
    }

    @Test
    void getRoleByName_OK() {
        role.setRoleName(Role.RoleName.ADMIN);
        Mockito.when(roleDao.getRoleByName(any())).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        assertNotNull(actual);
        assertEquals("ADMIN", actual.getRoleName().name());
    }

    @Test
    void findAll() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(Role.RoleName.ADMIN));
        roles.add(new Role(Role.RoleName.USER));
        Mockito.when(roleDao.findAll()).thenReturn(roles);
        List<Role> actual = roleService.findAll();
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals("ADMIN", actual.get(0).getRoleName().name());
        assertEquals("USER", actual.get(1).getRoleName().name());
    }
}
