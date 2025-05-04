package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleServiceImplTest {
    private final RoleDao roleDao = mock(RoleDao.class);

    private RoleService roleService;
    private Role role;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_validRole_ok() {
        Role saved = new Role(Role.RoleName.USER);
        saved.setId(1L);
        when(roleDao.save(role)).thenReturn(saved);

        Role actual = roleService.save(role);
        assertNotNull(actual);
        assertEquals(saved, actual);
    }

    @Test
    void save_roleIsNull_notOk() {
        when(roleDao.save(null)).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class, () -> roleService.save(null));
    }

    @Test
    void getRoleByName_existingRole_ok() {
        Role roleFromDB = new Role(Role.RoleName.USER);
        roleFromDB.setId(1L);
        when(roleDao.getRoleByName(role.getRoleName().name())).thenReturn(Optional.of(roleFromDB));

        Role actual = roleService.getRoleByName(role.getRoleName().name());
        assertNotNull(actual);
        assertEquals(roleFromDB, actual);
    }

    @Test
    void getRoleByName_roleNotFound_notOk() {
        when(roleDao.getRoleByName(Role.RoleName.ADMIN.name())).thenReturn(Optional.empty());
        assertThrows(
                NoSuchElementException.class,
                () -> roleService.getRoleByName(Role.RoleName.ADMIN.name())
        );
    }

    @Test
    void getRoleByName_invalidRoleName_notOk() {
        when(roleDao.getRoleByName("DefinitelyInvalidRoleName"))
                .thenThrow(DataProcessingException.class);
        assertThrows(
                DataProcessingException.class,
                () -> roleService.getRoleByName("DefinitelyInvalidRoleName")
        );
    }

    @Test
    void getRoleByName_roleNameIsNull_notOk() {
        when(roleDao.getRoleByName(null)).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class, () -> roleService.getRoleByName(null));
    }
}
