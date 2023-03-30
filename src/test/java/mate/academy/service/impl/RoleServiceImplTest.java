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
    private static final String VALID_ROLE_NAME = "USER";
    private static final String INVALID_ROLE_NAME = "BOSS";
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
    void save_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(VALID_ROLE_NAME)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(VALID_ROLE_NAME);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByInvalidName_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(INVALID_ROLE_NAME));
    }
}
