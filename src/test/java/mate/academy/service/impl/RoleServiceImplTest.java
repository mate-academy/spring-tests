package mate.academy.service.impl;

import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    private static final String ROLE_NAME = "USER";
    private static final String NOT_VALID_ROLE_NAME = "NOT_VALID";
    private static final long ROLE_ID = 1L;
    private RoleService roleService;
    private Role testRole;
    private Role expected;
    @Mock
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleDao);
        testRole = new Role(USER);
        expected = new Role(USER);
        expected.setId(ROLE_ID);
    }

    @Test
    void save_validRole_ok() {
        when(roleDao.save(testRole)).thenReturn(expected);
        Role actual = roleService.save(testRole);
        assertNotNull(actual,
                "method should not return null if Role is valid");
        Assertions.assertEquals(ROLE_ID, actual.getId(),
                "method should return Role with actual Role id");
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName(),
                "method should return Role with actual Role name");
    }

    @Test
    void getRoleByName_validRoleName_ok() {
        when(roleDao.getRoleByName(ROLE_NAME)).thenReturn(Optional.of(expected));
        Role actual = roleService.getRoleByName(ROLE_NAME);
        assertNotNull(actual, "method should not return null if Role name is valid");
        Assertions.assertEquals(ROLE_ID, actual.getId(),
                "method should return Role with actual Role id");
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName(),
                "method should return Role with actual Role name");
    }

    @Test
    void getRoleByName_notValidOrNullRoleName_notOk() {
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName(null),
                "method should throw NoSuchElementException if Role name is null");
        assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(NOT_VALID_ROLE_NAME),
                "method should throw NoSuchElementException if Role name is not valid");
    }
}
