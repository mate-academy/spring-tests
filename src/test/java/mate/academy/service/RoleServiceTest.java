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
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private static final String INVALID_ROLE_NAME = "INVALID_NAME";
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void saveRole_Ok() {
        Mockito.when(roleDao.save(USER_ROLE)).thenReturn(USER_ROLE);
        Role actualRole = roleService.save(USER_ROLE);
        Assertions.assertEquals(USER_ROLE, actualRole);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(USER_ROLE.getRoleName().name()))
                .thenReturn(Optional.of(USER_ROLE));
        Role actualRole = roleService.getRoleByName(USER_ROLE.getRoleName().name());
        Assertions.assertEquals(USER_ROLE, actualRole);
    }

    @Test
    void getRoleByName_invalidName_notOk() {
        Mockito.when(roleDao.getRoleByName(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(INVALID_ROLE_NAME));
    }
}
