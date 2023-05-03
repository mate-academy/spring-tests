package mate.academy.service.impl;

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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    private static RoleService roleService;
    @Mock
    private static RoleDao roleDao;

    @BeforeEach
    public void setUp() {
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void getRoleByName_roleExists_ok() {
        String roleName = "USER";
        Role role = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.of(role));
        Assertions.assertEquals(role, roleService.getRoleByName(roleName),
                "Method should return role: %s for roleName: %s\n"
                        .formatted(role, roleName));
    }

    @Test
    void getRoleByName_roleDoesNotExists_notOk() {
        String roleName = "INVALID_ROLE_NAME";
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(roleName),
                "Method should throw %s when invalid role name is passed\n"
                        .formatted(NoSuchElementException.class));
    }
}
