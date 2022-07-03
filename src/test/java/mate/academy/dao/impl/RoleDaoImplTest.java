package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest{
    private Role roleAdmin;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleAdmin = new Role(Role.RoleName.ADMIN);
    }
    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }
    @Test
    void save_Ok() {
        Role actual = roleDao.save(roleAdmin);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(roleAdmin,actual);
    }
    @Test
    void getRoleByName_Ok() {
        Optional<Role> actual = roleDao.getRoleByName("ADMIN");
        assertNotNull(actual);
    }
    @Test
    void getRoleByName_notOk() {
        assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("non-existent_role"));
    }
}
