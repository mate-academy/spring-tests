package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_validRole_ok() {
        Mockito.when(roleDao.save(Mockito.any(Role.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
        Role expected = new Role(Role.RoleName.USER);
        Role actual = roleService.save(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_validName_ok() {
        Role expected = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.getRoleByName(Mockito.anyString())).thenReturn(Optional.of(expected));
        Role actual = roleService.getRoleByName(Role.RoleName.USER.name());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_invalidName_notOk() {
        Mockito.when(roleDao.getRoleByName(Mockito.anyString()))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName("notExistedRole"));
    }
}
