package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RoleServiceImplTest {
    private RoleDao roleDao = Mockito.mock(RoleDao.class);
    private RoleService roleService = new RoleServiceImpl(roleDao);
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        assertNotNull(actual);
        assertEquals(role, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(Role.RoleName.USER.name());
        assertEquals(role, actual);
    }

    @Test
    void getRoleByName_Not_Ok() {
        assertThrows(NoSuchElementException.class, () ->
                roleService.getRoleByName("Wrong roleName"));
    }
}
