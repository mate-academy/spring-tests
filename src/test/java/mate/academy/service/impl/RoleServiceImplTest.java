package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final Role.RoleName USER_ROLE = Role.RoleName.USER;
    private static final String NOT_EXISTING_ROLE = "MODERATOR";
    private RoleService roleService;
    private RoleDao roleDao;
    private Role userRole;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDaoImpl.class);
        roleService = new RoleServiceImpl(roleDao);
        userRole = new Role(Role.RoleName.USER);
    }

    @Test
    void save_validRole_Ok() {
        //arrange
        Mockito.when(roleDao.save(userRole)).thenReturn(userRole);

        //act
        Role actual = roleService.save(userRole);

        //assert
        Assertions.assertEquals(USER_ROLE, actual.getRoleName());
        Mockito.verify(roleDao).save(userRole);
    }

    @Test
    void getRoleByName_roleExist_Ok() {
        //arrange
        Mockito.when(roleDao.getRoleByName(USER_ROLE.name())).thenReturn(Optional.of(userRole));

        //act
        Role actual = roleService.getRoleByName(USER_ROLE.name());

        //assert
        Assertions.assertEquals(USER_ROLE, actual.getRoleName());
        Mockito.verify(roleDao).getRoleByName(actual.getRoleName().name());
    }

    @Test
    void getRoleByName_roleByNotExistName_NotOk() {
        //arrange
        Mockito.when(roleDao.getRoleByName(NOT_EXISTING_ROLE))
                .thenThrow(new NoSuchElementException());

        //act & assert
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleDao.getRoleByName(NOT_EXISTING_ROLE).orElseThrow());
        Mockito.verify(roleDao).getRoleByName(NOT_EXISTING_ROLE);
    }

    @Test
    void getRoleByName_roleByNullName_NotOk() {
        //arrange
        Mockito.when(roleDao.getRoleByName(null))
                .thenThrow(new NoSuchElementException());

        //act & assert
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleDao.getRoleByName(null).orElseThrow());
        Mockito.verify(roleDao).getRoleByName(null);
    }
}
