package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RoleServiceImplTest {

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveRole_OK() {
        Role role = new Role(Role.RoleName.USER);

        when(roleDao.save(role)).thenReturn(role);

        Role savedRole = roleService.save(role);

        assertNotNull(savedRole);
        assertEquals(role, savedRole);
        verify(roleDao, times(1)).save(role);
    }

    @Test
    void testGetRoleByName_OK() {
        Role role = new Role(Role.RoleName.USER);
        String roleName = "USER";

        when(roleDao.getRoleByName(roleName)).thenReturn(Optional.of(role));

        Role retrievedRole = roleService.getRoleByName(roleName);

        assertNotNull(retrievedRole);
        assertEquals(role, retrievedRole);
        verify(roleDao, times(1)).getRoleByName(roleName);
    }

    @Test
    void testGetRoleByNameNotFound_Not_OK() {
        String roleName = "ADMIN";

        when(roleDao.getRoleByName(roleName)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName(roleName));
        verify(roleDao, times(1)).getRoleByName(roleName);
    }

    @Test
    void testGetRoleByNameInvalidRoleName_Not_OK() {
        String roleName = "INVALID_ROLE";

        when(roleDao.getRoleByName(roleName)).thenThrow(DataProcessingException.class);

        assertThrows(DataProcessingException.class, () -> roleService.getRoleByName(roleName));
        verify(roleDao, times(1)).getRoleByName(roleName);
    }

    @Test
    void testGetRoleByNonExistName_OK() {
        String roleName = "UgEr";

        when(roleDao.getRoleByName(roleName)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName(roleName));

        verify(roleDao, times(1)).getRoleByName(roleName);
    }
}
