package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class RoleServiceImplTest {
    private RoleService roleService;
    @Mock
    private RoleDao roleDao;
    private Role user;
    private Role admin;
    private static final String USER_ROLE_NAME = "USER";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleService = new RoleServiceImpl(roleDao);
        user = new Role(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Mockito.when(roleDao.save(user)).thenReturn(user);
        Role userActual = roleService.save(user);
        Assertions.assertEquals(userActual.getRoleName(), user.getRoleName());
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(USER_ROLE_NAME)).thenReturn(Optional.of(user));
        Role actualRole = roleService.getRoleByName(USER_ROLE_NAME);
        Assertions.assertEquals(actualRole.getRoleName().name(), USER_ROLE_NAME);
    }

    @Test
    void getRoleByName_wrongRoleName_notOk() {
        Mockito.when(roleDao.getRoleByName("Userling")).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(""));
    }

    @Test
    void getRoleByName_emptyRoleName_notOk() {
        Mockito.when(roleDao.getRoleByName("")).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(""));
    }

    @Test
    void getRoleByName_nullRoleName_notOk() {
        Mockito.when(roleDao.getRoleByName(null)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(null));
    }
}
