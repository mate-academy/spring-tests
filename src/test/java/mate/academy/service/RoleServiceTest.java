package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    private static final String CORRECT_USER_NAME = "USER";
    private static final String FAKE_USER_NAME = "FAKE";
    @Mock
    private RoleDao roleDao;
    private RoleService roleService;
    private Role role;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId());
        Assertions.assertEquals(CORRECT_USER_NAME, actual.getRoleName().name());
    }

    @Test
    void getRoleByName_roleExists_Ok() {
        Mockito.when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(CORRECT_USER_NAME);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(CORRECT_USER_NAME, actual.getRoleName().name());
    }

    @Test
    void getRoleByName_NoSuchElementException_NotOk() {
        Mockito.when(roleDao.getRoleByName(FAKE_USER_NAME)).thenThrow(new NoSuchElementException());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(FAKE_USER_NAME));
    }
}
