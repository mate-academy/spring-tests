package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final String USER_ROLE_NAME = "USER";
    private static final String ADMIN_ROLE_NAME = "ADMIN";
    private static final String WRONG_ROLE_NAME = "WRONG";
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.spy(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Role test = new Role();
        test.setId(1L);
        test.setRoleName(Role.RoleName.USER);
        Mockito.when(roleDao.save(role)).thenReturn(test);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getId(), 1L);
        Assertions.assertEquals(actual.getRoleName(), test.getRoleName());
    }

    @Test
    void getRoleByName_User_Ok() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);
        Mockito.when(roleDao.getRoleByName(Mockito.any()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(USER_ROLE_NAME);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_Admin_Ok() {
        Role role = new Role();
        role.setId(2L);
        role.setRoleName(Role.RoleName.ADMIN);
        Mockito.when(roleDao.getRoleByName(Mockito.any()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(ADMIN_ROLE_NAME);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_RoleNameNotExist() {
        try {
            roleService.getRoleByName(WRONG_ROLE_NAME);
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present",
                    e.getMessage());
        }
    }
}
