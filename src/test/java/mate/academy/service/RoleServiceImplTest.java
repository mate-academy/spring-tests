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

public class RoleServiceImplTest {
    public static final String USER_ROLE = "USER";
    public static final String WRONG_ROLE = "SUPERUSER";
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
    void saveMethod_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertEquals(role, actual, "Role saved incorrectly");
    }

    @Test
    void getRoleByNameMethod_Ok() {
        Mockito.when(roleDao.getRoleByName(USER_ROLE)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(USER_ROLE);
        Assertions.assertNotNull(actual, "getRoleByName Method must "
                + "return role with correct credentials");
        Assertions.assertEquals(actual.getRoleName(), Role.RoleName.USER,
                "Incorrect role returned");
    }

    @Test
    void getRoleByNameMethod_NotOk() {
        try {
            Role actual = roleService.getRoleByName(WRONG_ROLE);
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive NoSuchElementException: No value present");
    }
}
