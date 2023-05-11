package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.NoSuchElementException;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);

    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        Role roleByName = roleDao.getRoleByName(role.getRoleName().name())
                .orElseThrow(() -> new NoSuchElementException("Couldn`t get role by name "
                        + role.getRoleName()));
        assertEquals(role.getRoleName(), roleByName.getRoleName());
    }

    @Test
    void getRoleByName_NoSuchElementException_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleDao.getRoleByName("ADMIN").get());
    }
}
