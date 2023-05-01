package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleDaoImplTest extends AbstractTest {
    private String valid = "bob@i.ua";
    private String invalid = "else@i.ua";
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_OK() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(valid);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(role.getRoleName(), actual.get().getRoleName());
    }

    @Test
    void getRoleByName_IncorrectName_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(invalid));
    }

    @Test
    void getRoleByName_RoleNameIsNull_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.getRoleByName(null));
    }
}
