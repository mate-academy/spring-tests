package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Optional;

class RoleDaoImplTest extends AbstractTest {
    private static final String ROLE_USER = "USER";
    private Role role;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        role = new Role();
        roleDao = new RoleDaoImpl(getSessionFactory());
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(new Role(Role.RoleName.USER));
    }

    @Test
    void getRoleByName_Ok() {
        Optional<Role> actual = roleDao.getRoleByName(ROLE_USER);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getRoleByName_notOk() {
        assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName("Fail_Role"));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }
}
