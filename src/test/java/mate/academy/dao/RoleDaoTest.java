package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private String correctRoleName;
    private String incorrectRoleName;
    private Long identifier;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        correctRoleName = Role.RoleName.USER.name();
        incorrectRoleName = "SASHA";
        identifier = 1L;
    }

    @Test
    void save_Ok() {
        Role role = new Role(Role.RoleName.USER);
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual, "Role must not be null for input role: " + role);
        Assertions.assertEquals(identifier, actual.getId(), "Identifier must be: "
                + identifier + " but was: " + actual.getId());
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(correctRoleName);
        Assertions.assertNotNull(actual, "Role must not be null for input role name: " + correctRoleName);
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName(),
                "Expected: " + role.getRoleName() + " but was: " + actual.get().getRoleName());
    }

    @Test
    void getRoleByName_NotOk() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        Assertions.assertThrows(DataProcessingException.class,
                () -> {roleDao.getRoleByName(incorrectRoleName);},
                "Expected DataProcessingException for input role name: " + incorrectRoleName);
    }
}