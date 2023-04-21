package mate.academy.service.impl;

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
    private static final String VALID_USER_ROLE = "USER";
    private static final String INVALID_USER_ROLE = "ANTIUSER";
    private RoleDao roleDao;
    private RoleService roleService;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(VALID_USER_ROLE)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(VALID_USER_ROLE);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_InvalidName_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            roleService.getRoleByName(INVALID_USER_ROLE);
        }, "This role name is invalid");
    }
}
