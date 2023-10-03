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
    private static final String TEST_ROLE = "USER";
    private Role role;
    private RoleDao roleDao;
    private RoleService roleService;

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
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName(),
                String.format("Expected role should be %s, but was %s",
                        role.getRoleName(), actual.getRoleName()));
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(TEST_ROLE)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(TEST_ROLE);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_ROLE, actual.getRoleName().name(),
                String.format("Expected role's name should be %s, but was %s",
                        TEST_ROLE, actual.getRoleName().name()));
    }

    @Test
    void getRoleByName_nonExistedRole_notOk() {
        Mockito.when(roleDao.getRoleByName("UNKNOWN")).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () ->
                roleService.getRoleByName("UNKNOWN"),
                "Expected NoSuchElementException to be thrown");
    }
}
