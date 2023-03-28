package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final String UNSAVED_ROLE_NAME = Role.RoleName.ADMIN.name();
    private static RoleServiceImpl roleService;

    @BeforeAll
    static void beforeAll() {
        RoleDao roleDao = Mockito.mock(RoleDaoImpl.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void getRoleByName_unsavedRoleNotNull_notOk() {
        assertThrows(
                NoSuchElementException.class,
                () -> roleService.getRoleByName(UNSAVED_ROLE_NAME),
                "Expected to throw NoSuchElementException for unsaved/unexisting role"
        );
    }
}
