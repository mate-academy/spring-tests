package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private final Role role = new Role(Role.RoleName.USER);

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDaoImpl.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role saveRole = roleService.save(role);
        Assertions.assertNotNull(saveRole);
        Assertions.assertEquals(role.getRoleName(), saveRole.getRoleName());
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(role.getRoleName().toString()))
                .thenReturn(Optional.of(role));
        Role user = roleService.getRoleByName("USER");
        Assertions.assertNotNull(user);
        Assertions.assertEquals("USER", user.getRoleName().toString());
    }

    @Test
    void getRoleByName_incorrectRoleName_notOk() {
        String massageException = "No value present";
        Mockito.when(roleDao.getRoleByName(role.getRoleName().toString()))
                .thenReturn(Optional.empty());
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName("INCORRECT_ROLE"));
        Assertions.assertEquals(massageException, exception.getMessage());
    }
}
