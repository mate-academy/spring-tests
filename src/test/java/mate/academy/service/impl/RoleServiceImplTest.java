package mate.academy.service.impl;

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
    private static final long USER_ID = 1L;
    private static final String EXIST_ROLE = "USER";
    private static final String NON_EXIST_ROLE = "CUSTOMER";
    private Role role;
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(USER_ID);
        role.setRoleName(Role.RoleName.USER);
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_roleExists_Ok() {
        Mockito.when(roleDao.getRoleByName(EXIST_ROLE)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_roleNotExists_notOk() {
        try {
            roleService.getRoleByName(NON_EXIST_ROLE);
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive NoSuchElementException");
    }
}
