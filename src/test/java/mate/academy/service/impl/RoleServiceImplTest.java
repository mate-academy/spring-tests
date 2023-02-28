package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final String ROLE_MANE = "USER";
    private RoleService roleService;
    private RoleDao roleDao;
    private Role expected;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);

        expected = new Role();
        expected.setRoleName(RoleName.USER);
    }

    @Test
    void save_ok() {
        Mockito.when(roleDao.save(any())).thenReturn(expected);

        Role actual = roleService.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(ROLE_MANE)).thenReturn(Optional.ofNullable(expected));

        Role actual = roleService.getRoleByName(ROLE_MANE);

        assertEquals(expected, actual);
    }

    @Test
    void getRoleByName_roleNameNull_notOk() {
        assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(null));
    }

    @Test
    void getRoleByName_roleNotExistInDb_notOk() {
        assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(ROLE_MANE));
    }
}
