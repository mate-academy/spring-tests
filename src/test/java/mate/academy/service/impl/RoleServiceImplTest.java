package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleServiceImplTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        role.setId(1L);
    }

    @Test
    void save_ok() {
        when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(Role.RoleName.USER.name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByName_ok() {
        when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void getRoleByName_invalidRoleName_notOk() {
        when(roleDao.getRoleByName("NonExistingRole"))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                roleService.getRoleByName("NonExistingRole"));
    }
}
