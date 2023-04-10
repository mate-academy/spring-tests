package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private static final String ROLE_NAME = "USER";
    private static final String WRONG_ROLE_NAME = "HUMAN";
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
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getRoleName(), role.getRoleName());
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(ROLE_NAME)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(ROLE_NAME);
        Assertions.assertEquals(ROLE_NAME, actual.getRoleName().name());
    }

    @Test
    void getRoleByName_wrongRoleName_notOk() {
        Mockito.when(roleDao.getRoleByName(WRONG_ROLE_NAME)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(WRONG_ROLE_NAME));
    }
}
