package mate.academy.service.impl;

import static mate.academy.model.Role.RoleName.USER;

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
    void save_ok() {
        Role role = new Role();
        role.setRoleName(USER);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_ok() {
        Role role = new Role();
        role.setRoleName(USER);
        role.setId(1L);
        Mockito.when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId().longValue());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_roleNameDoesNotExist_notOk() {
        Mockito.when(roleDao.getRoleByName("USER")).thenReturn(Optional.empty());
        Throwable exception = Assertions.assertThrows(NoSuchElementException.class, () -> {
            roleService.getRoleByName("USER");
        }, "NoSuchElementException was expected");
        Assertions.assertEquals("No value present", exception.getLocalizedMessage());
    }
}
