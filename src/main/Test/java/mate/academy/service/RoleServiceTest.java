package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private static final String ROLE ="USER";
    private static final String INVALID_ROLE ="GUEST";
    private RoleDao roleDao;
    private RoleService roleService;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(Role.RoleName.USER);
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(ROLE))
                .thenReturn(Optional.ofNullable(role));
        Role actual = roleService.getRoleByName(ROLE);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_roleNameIsNotExsist_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(INVALID_ROLE));
    }
}
