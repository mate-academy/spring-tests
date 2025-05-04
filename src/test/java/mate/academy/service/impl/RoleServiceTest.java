package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private static final String ROLE_NAME_USER = "USER";
    private static final String ROLE_NAME_TEST = "TEST";
    private static RoleService roleService;
    private static RoleDao roleDao;
    private Role testRole;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @BeforeEach
    void setUp() {
        testRole = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(testRole)).thenReturn(testRole);
        Role actual = roleService.save(testRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testRole, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(ROLE_NAME_USER)).thenReturn(Optional.of(testRole));
        Role actual = roleService.getRoleByName(ROLE_NAME_USER);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testRole, actual);
    }

    @Test
    void getRoleByName_notOk() {
        try {
            Role actual = roleService.getRoleByName(ROLE_NAME_TEST);
        } catch (Exception e) {
            Assertions.assertEquals(NoSuchElementException.class, e.getClass());
            return;
        }
        Assertions.fail("Expected to receive NoSuchElementException");
    }
}
