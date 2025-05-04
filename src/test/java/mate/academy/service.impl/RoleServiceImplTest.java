package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final String UNSAVED_ROLE_NAME = Role.RoleName.ADMIN.name();
    private static RoleServiceImpl roleService;
    private static RoleDao roleDao;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDaoImpl.class);
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
    void getRoleByName_unsavedRole_notOk() {
        assertThrows(
                NoSuchElementException.class,
                () -> roleService.getRoleByName(UNSAVED_ROLE_NAME),
                "Expected to throw NoSuchElementException for unsaved/unexisting role"
        );
    }
}
