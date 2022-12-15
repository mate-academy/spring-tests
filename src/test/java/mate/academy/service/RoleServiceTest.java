package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private static final Role.RoleName DEFAULT_ROLE = Role.RoleName.USER;
    private static RoleService roleService;
    private static RoleDao roleDao;
    private static Role role;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role();
    }

    @BeforeEach
    void setUp() {
        role.setRoleName(DEFAULT_ROLE);
    }

    @Test
    void save_UserOk() {
        Mockito.when(roleDao.save(role)).thenReturn(role);

        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));

        Role actual = roleService.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_NoSuchElement() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                roleService.getRoleByName("INVALID_ROLE"));
    }
}
