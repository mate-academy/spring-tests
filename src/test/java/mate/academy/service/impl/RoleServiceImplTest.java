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
    private static final String ROLE_NAME_USER = "USER";
    private static final String INCORRECT_ROLE_NAME = "SUPER_ADMIN";
    private RoleService roleService;
    private RoleDao roleDao;
    private Role user;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        user = new Role(Role.RoleName.valueOf(ROLE_NAME_USER));
    }

    @Test
    void save_correctData_Ok() {
        Mockito.when(roleDao.save(user)).thenReturn(user);
        Role actual = roleService.save(user);
        Assertions.assertEquals(user.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_correctData_Ok() {
        Mockito.when(roleDao.getRoleByName(ROLE_NAME_USER)).thenReturn(Optional.of(user));
        Optional<Role> actual = roleDao.getRoleByName(user.getRoleName().toString());
        Assertions.assertNotNull(actual);
    }

    @Test
    void getRoleByName_incorrectRole_NoSuchElementException() {
        Mockito.when(roleDao.getRoleByName(INCORRECT_ROLE_NAME)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            roleService.getRoleByName(ROLE_NAME_USER);
        });
    }
}
