package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    private static Role USER = new Role(Role.RoleName.USER);
    private static Role ADMIN = new Role(Role.RoleName.ADMIN);
    @Mock
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_userRole_ok() {
        Role expected = new Role(1L, Role.RoleName.USER);
        when(roleDao.save(any())).thenReturn(expected);
        Role actual = roleService.save(USER);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void save_adminRole_ok() {
        Role expected = new Role(1L, Role.RoleName.ADMIN);
        when(roleDao.save(any())).thenReturn(expected);
        Role actual = roleService.save(ADMIN);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByName_validRole_ok() {
        Role expected = new Role(1L, Role.RoleName.ADMIN);
        when(roleDao.getRoleByName(any())).thenReturn(Optional.of(expected));
        Role actual = roleService.getRoleByName(ADMIN.getRoleName().name());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByName_noSuchRole_notOk() {
        when(roleDao.getRoleByName(any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> roleService.getRoleByName(ADMIN.getRoleName().name()));
    }
}
