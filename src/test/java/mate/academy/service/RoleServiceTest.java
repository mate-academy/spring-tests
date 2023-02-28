package mate.academy.service;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RoleServiceTest {
    private static final Role.RoleName ROLE_USER = Role.RoleName.USER;
    private static RoleService roleService;
    private static RoleDao roleDao;
    private static Role role;

    @BeforeAll
    static void beforeAll() {
        role = new Role();
        role.setRoleName(ROLE_USER);
    }

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(ROLE_USER.name())).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(ROLE_USER.name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ROLE_USER.name(), actual.getRoleName().name());
    }
}
