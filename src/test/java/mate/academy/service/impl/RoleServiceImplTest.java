package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final Long ID = 1L;
    private RoleService roleService;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);

        role = new Role();
        role.setRoleName(Role.RoleName.USER);

    }

    @Test
    void save_ok() {
        when(roleDao.save(role)).thenReturn(new Role(ID, Role.RoleName.USER));

        Role actual = roleService.save(role);
        assertNotNull(actual);
        assertEquals(ID, actual.getId());
    }

    @Test
    void getRoleByName_ok() {
        when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(new Role(ID, Role.RoleName.USER)));

        Role actual = roleService.getRoleByName(Role.RoleName.USER.name());
        assertNotNull(actual);
        assertEquals(ID, actual.getId());
    }

    @Test
    void getRoleByName_notOk() {
        when(roleDao.getRoleByName(Role.RoleName.ADMIN.name()))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            roleService.getRoleByName(Role.RoleName.ADMIN.name());
        }, "NoSuchElementException expected");
    }
}
