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
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void saveRole_Ok() {
        Role role = new Role(Role.RoleName.USER);
        role.setId(1L);
        Mockito.when(roleDao.save(role))
                .thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName("USER"))
                .thenReturn(Optional.of(new Role(Role.RoleName.USER)));
        Role actual = roleService.getRoleByName("USER");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void getRoleByName_invalidName_notOk() {
        try {
            roleService.getRoleByName("ADMIN");
        } catch (NoSuchElementException e) {
            return;
        }

        Assertions.fail("Expected to get NoSuchElementException");
    }
}
