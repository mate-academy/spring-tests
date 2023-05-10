package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class RoleServiceImplTest {
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private RoleService roleService;

    @BeforeEach
    public void setUp() {
        RoleDao roleDao = Mockito.mock(RoleDao.class);
        Mockito.when(roleDao.save(ROLE))
                .thenReturn(new Role(Role.RoleName.USER));
        Mockito.when(roleDao.getRoleByName(ROLE.getRoleName().name()))
                .thenReturn(Optional.of(ROLE));
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    public void save_Ok() {
        Role actual = roleService.save(ROLE);
        assertEquals(ROLE.getRoleName(), actual.getRoleName());
    }

    @Test
    public void getRoleByName_Ok() {
        Role actual = roleService.getRoleByName(ROLE.getRoleName().name());
        assertEquals(ROLE.getRoleName(), actual.getRoleName());
    }

    @Test
    public void getRoleByName_NotOK() {
        assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName("CUSTOMER"));
    }
}
