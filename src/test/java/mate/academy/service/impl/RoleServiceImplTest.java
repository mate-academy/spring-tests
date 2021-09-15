package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest extends AbstractTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private Role expected;
    private Role.RoleName CORRECT_ROLE_NAME = Role.RoleName.USER;
    private String INVALID_ROLE_NAME = "UsEr";

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDaoImpl.class);
        roleService = new RoleServiceImpl(roleDao);

        expected = new Role();
        expected.setRoleName(CORRECT_ROLE_NAME);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(expected)).thenReturn(expected);

        Role actual = roleService.save(expected);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(CORRECT_ROLE_NAME.name()))
                .thenReturn(Optional.of(expected));
        roleService.save(expected);

        Role actual = roleService.getRoleByName(CORRECT_ROLE_NAME.name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_NotOk() {
        Mockito.when(roleDao.getRoleByName(Mockito.any()))
                .thenReturn(Optional.empty());
        try {
            roleService.getRoleByName(INVALID_ROLE_NAME);
        } catch (Exception e) {
            return;
        }
        Assertions.fail("Exception must be thrown");
    }
}
