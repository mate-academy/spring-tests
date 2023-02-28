package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final String USER_ROLE_NAME = "USER";
    private RoleDao roleDao;
    private Role expected;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        expected = new Role();
        expected.setRoleName(RoleName.USER);
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    void getRoleByName_ok() {
        roleDao.save(expected);

        Optional<Role> actual = roleDao.getRoleByName(USER_ROLE_NAME);

        assertEquals(Optional.of(expected), actual);
    }

    @Test
    void getRoleByName_roleNull_notOk() {
        assertThrows(RuntimeException.class, ()
                -> roleDao.getRoleByName(null));
    }

    @Test
    void getRoleByName_roleNotAdded_notOk() {
        assertThrows(RuntimeException.class, ()
                -> roleDao.getRoleByName(USER_ROLE_NAME).get());
    }
}
