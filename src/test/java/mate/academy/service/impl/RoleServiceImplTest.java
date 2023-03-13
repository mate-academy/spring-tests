package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final String USER_ROLE_NAME = "USER";
    private RoleService roleService;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.save(role)).thenReturn(role);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(USER_ROLE_NAME)).thenReturn(Optional.of(role));
        Optional<Role> actual = roleDao.getRoleByName(USER_ROLE_NAME);
        assertNotNull(actual);
        assertEquals(role, actual.get());
    }

    @Test
    void getRoleByName_NotOk() {
        assertThrows(RuntimeException.class,
                () -> roleService.getRoleByName(null));
    }
}
