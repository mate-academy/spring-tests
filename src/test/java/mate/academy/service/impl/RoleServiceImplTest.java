package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final String ROLE_NAME_ADMIN = "ADMIN";
    private static final Role ROLE_CLASS_ADMIN = new Role(Role.RoleName.ADMIN);
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(ROLE_CLASS_ADMIN)).thenReturn(ROLE_CLASS_ADMIN);
        Role actual = roleService.save(ROLE_CLASS_ADMIN);
        assertNotNull(actual);
        assertEquals(ROLE_CLASS_ADMIN, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(ROLE_NAME_ADMIN))
                .thenReturn(Optional.of(ROLE_CLASS_ADMIN));
        Role actual = roleService.getRoleByName(ROLE_NAME_ADMIN);
        assertNotNull(actual);
        assertEquals(ROLE_CLASS_ADMIN, actual);
    }

    @Test
    void getRoleByName_Causes_Exception_Not_Ok() {
        Mockito.when(roleDao.getRoleByName(any())).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class,
                () -> roleService.getRoleByName(ROLE_NAME_ADMIN));
    }
}
