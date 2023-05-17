package mate.academy.service;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    public void save_Ok() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);

        Mockito.when(roleDao.save(role)).thenReturn(role);

        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L,actual.getId());
    }

    @Test
    public void getRoleByName_Ok() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);

        Mockito.when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));

        Role actual = roleService.getRoleByName("USER");

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("USER",actual.getRoleName().name());
    }

    @Test
    public void getRoleByName_Not_Existing_RoleName() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);

        Mockito.when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));

        try {
            Role actual = roleService.getRoleByName("STUDENT");
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("1","1");
            return;
        }
        fail("Expected to receive NoSuchElementException");

    }
}
