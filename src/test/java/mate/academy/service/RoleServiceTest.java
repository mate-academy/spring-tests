package mate.academy.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleServiceTest {
    private static final Long ID = 1L;
    private static final Long SECOND_ID = 2L;
    private Role role;
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role();
        role.setId(ID);
    }

    @Test
    void save_role_ok() {
        role.setRoleName(Role.RoleName.USER);
        when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        assertNotNull(actual);
        assertEquals(ID, actual.getId());
        assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void getRoleByName_existedRoles_ok() {
        role.setRoleName(Role.RoleName.ADMIN);
        when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        assertNotNull(actual);
        assertEquals(ID, actual.getId());
        assertEquals(Role.RoleName.ADMIN, actual.getRoleName());

        role.setId(SECOND_ID);
        role.setRoleName(Role.RoleName.USER);
        when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));
        actual = roleService.getRoleByName(role.getRoleName().name());
        assertNotNull(actual);
        assertEquals(SECOND_ID, actual.getId());
        assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void getRoleByName_notSavedRole_notOk() {
        assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(Role.RoleName.ADMIN.name()));
    }
}
