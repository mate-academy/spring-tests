package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RoleServiceImplTest {
    private static final Long ID = 1L;
    private static final Role.RoleName USER_ROLE = Role.RoleName.USER;
    private static RoleService roleService;
    private static RoleDao roleDao;
    private Role role;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(USER_ROLE);
    }

    @Test
    void save_ok() {
        when(roleDao.save(role)).thenReturn(new Role(ID, USER_ROLE));

        Role actual = roleService.save(role);
        assertNotNull(actual);
        assertEquals(ID, actual.getId());
    }

    @Test
    void getRoleByName_ok() {
        when(roleDao.getRoleByName(USER_ROLE.name()))
                .thenReturn(Optional.of(new Role(ID, USER_ROLE)));

        Role actual = roleService.getRoleByName(USER_ROLE.name());
        assertNotNull(actual);
        assertEquals(USER_ROLE, actual.getRoleName());
        assertEquals(ID, actual.getId());
    }

    @Test
    void getRoleByName_nonExistentRole_notOk() {
        when(roleDao.getRoleByName(anyString())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            roleService.getRoleByName(USER_ROLE.name());
        }, "NoSuchElementException is expected");
    }
}
