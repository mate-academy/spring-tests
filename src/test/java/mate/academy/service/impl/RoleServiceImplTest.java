package mate.academy.service.impl;

import static org.mockito.ArgumentMatchers.any;

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
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_ok() {
        Mockito.when(roleDao.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Role actual = roleService.save(ROLE);
        Assertions.assertNotNull(actual, "Role shouldn't be null");
        Assertions.assertEquals(actual, ROLE,
                String.format("Should return role: %s, but was: %s", ROLE, actual));
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(any())).thenReturn(Optional.of(ROLE));
        Role actual = roleService.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertNotNull(actual, "Role shouldn't be null");
        Assertions.assertEquals(actual.getRoleName().name(), ROLE.getRoleName().name(),
                String.format("Should return role: %s, but was: %s", ROLE.getRoleName(),
                        actual.getRoleName().name()));
    }

    @Test
    void getRoleByName_notExistingRole_notOk() {
        Mockito.when(roleDao.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(Optional.of(ROLE));
        NoSuchElementException thrown
                = Assertions.assertThrows(NoSuchElementException.class, () -> {
                    roleService.getRoleByName("NOT_VALID_ROLE");
                });
        Assertions.assertEquals("No value present", thrown.getMessage(),
                "Should throw: \"java.util.NoSuchElementException: No value present\"");
    }
}
