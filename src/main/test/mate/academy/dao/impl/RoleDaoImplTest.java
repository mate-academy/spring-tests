package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    public void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    public void save_Ok() {
        Role actual = roleDao.save(role);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    public void getRoleByName_Ok() {
        roleDao.save(role);
        Optional<Role> actualOptional = roleDao.getRoleByName("USER");
        assertNotNull(actualOptional);
        assertFalse(actualOptional.isEmpty());
        Role actual = actualOptional.get();
        assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    public void getRoleByName_NotOk() {
        roleDao.save(role);
        assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("CUSTOMER"));
    }
}
