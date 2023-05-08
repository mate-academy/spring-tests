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

class RoleServiceTest {
    private static final String ROLE_NAME = "USER";
    private static final String ROLE_NAME_IS_NOT_IN_DB = "PHANTOM";
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
    void save_ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(ROLE_NAME))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(ROLE_NAME);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_roleNameIsNotInDb_ok() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(ROLE_NAME_IS_NOT_IN_DB));
    }
}
