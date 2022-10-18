package mate.academy.service;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final String USER_ROLE_NAME = "USER";
    private RoleService roleService;
    private RoleDao roleDao;
    private Role userRole;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        userRole = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(userRole)).thenReturn(userRole);
        Role actual = roleService.save(userRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userRole, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(USER_ROLE_NAME)).thenReturn(Optional.of(userRole));
        Optional<Role> actual = roleDao.getRoleByName(USER_ROLE_NAME);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userRole, actual.get());
    }

    @Test
    void getRoleByNameNull_NotOk() {
        Assertions.assertThrows(RuntimeException.class, () ->
                roleService.getRoleByName(null));
    }
}
