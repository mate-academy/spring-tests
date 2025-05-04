package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void saveCorrectRole_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Mockito.when(roleDao.save(role)).thenReturn(role);

        Role actual = roleService.save(role);
        assertNotNull(actual);
        assertEquals(role.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByExistName_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Mockito.when(roleDao.getRoleByName("USER")).thenReturn(Optional.ofNullable(role));

        Role actual = roleService.getRoleByName("USER");
        assertNotNull(actual);
        assertEquals(role.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByNotExistName_NotOk() {
        Mockito.when(roleDao.getRoleByName("GUEST")).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            roleService.getRoleByName("GUEST");
        }, "Expected to receive NoSuchElementException");
    }
}
