package mate.academy.service.impl;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final Role TEST_ROLE = new Role(Role.RoleName.USER);
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
        Role actual = roleService.save(TEST_ROLE);
        Assertions.assertNotNull(actual, "Role can't be null");
        Assertions.assertEquals(actual, TEST_ROLE,
                String.format("Expect role: %s, but was: %s", TEST_ROLE, actual));
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(any())).thenReturn(Optional.of(TEST_ROLE));
        Role actual = roleService.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertNotNull(actual, "Role can't be null");
        Assertions.assertEquals(actual.getRoleName().name(),
                TEST_ROLE.getRoleName().name(),
                String.format("Expect role: %s, but was: %s", TEST_ROLE.getRoleName(),
                        actual.getRoleName().name()));
    }
}
