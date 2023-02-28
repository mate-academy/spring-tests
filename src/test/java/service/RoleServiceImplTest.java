package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RoleServiceImplTest {
    private static final String ROLE_ADMIN = "ADMIN";
    private RoleService roleService;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDaoImpl.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.ADMIN);
        role.setId(1L);
    }

    @Test
    public void save_Ok() {
        Role newRole = new Role(Role.RoleName.ADMIN);
        Mockito.when(roleDao.save(newRole)).thenReturn(role);
        Role actual = roleService.save(newRole);
        assertNotNull(actual);
        assertEquals(actual, role);
    }

    @Test
    public void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(ROLE_ADMIN)).thenReturn(Optional.ofNullable(role));
        Role actual = roleService.getRoleByName(ROLE_ADMIN);
        assertNotNull(actual);
        assertEquals(actual, role);
    }
}
