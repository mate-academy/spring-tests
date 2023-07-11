package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
        ROLE.setId(null);
    }

    @Test
    void save_validRole_ok() {
        ROLE.setId(1L);
        Mockito.when(roleDao.save(ROLE)).thenReturn(ROLE);
        Role actual = roleService.save(ROLE);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void getRoleByName_validName_ok() {
        Mockito.when(roleDao.getRoleByName(ROLE.getRoleName().name()))
                .thenReturn(Optional.of(ROLE));
        Role actual = roleService.getRoleByName(ROLE.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ROLE.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_invalidName_notOk() {
        try {
            roleService.getRoleByName("dwadawdawd");
        } catch (NoSuchElementException e) {
            return;
        }
        Assertions.fail("getRoleByName() is expected to throw NoSuchElementException");
    }
}
