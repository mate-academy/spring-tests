package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

class RoleServiceImplTest {
    private static final String ROLE_NAME_ADMIN = "ADMIN";
    private static final String ROLE_NOT_EXISTENT_NAME = "MODERATOR";
    private static RoleService roleService;
    private static RoleDao roleDao;
    private static Role userRole;
    private static Role adminRole;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        userRole = new Role(Role.RoleName.USER);
        adminRole = new Role(Role.RoleName.ADMIN);
    }

    @Test
    public void saveRole_adminRole_Ok() {
        Mockito.when(roleDao.save(adminRole)).thenAnswer((Answer<Role>) invocationOnMock -> {
            Role grantedRole = (Role) invocationOnMock.getArguments()[0];
            grantedRole.setId(1L);
            return grantedRole;
        });
        Role savedRole = roleService.save(adminRole);
        Assertions.assertEquals(adminRole.getRoleName(),savedRole.getRoleName());
        Assertions.assertEquals(1L,savedRole.getId());
    }

    @Test
    public void saveRole_userRole_Ok() {
        Mockito.when(roleDao.save(userRole)).thenAnswer((Answer<Role>) invocationOnMock -> {
            Role grantedRole = (Role) invocationOnMock.getArguments()[0];
            grantedRole.setId(2L);
            return grantedRole;
        });
        Role savedRole = roleService.save(userRole);
        Assertions.assertEquals(userRole.getRoleName(),savedRole.getRoleName());
        Assertions.assertEquals(2L,savedRole.getId());
    }

    @Test
    public void getRoleByName_existentRole_Ok() {
        String roleName = ROLE_NAME_ADMIN;
        Mockito.when(roleDao.getRoleByName(roleName))
                .thenReturn(Optional.of(new Role(Role.RoleName.ADMIN)));
        Role actual = roleService.getRoleByName(roleName);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(Role.RoleName.valueOf(roleName),actual.getRoleName());
    }

    @Test
    public void getRoleByName_notExistentRole_noSuchElementException_NotOk() {
        String roleName = ROLE_NOT_EXISTENT_NAME;
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(roleName));
    }
}
