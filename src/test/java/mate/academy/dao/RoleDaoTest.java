package mate.academy.dao;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private static final Long ROLE_ID = 1L;
    private static final String USER_ROLE = "USER";
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Role actual = roleDao.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ROLE_ID, actual.getId(),
                String.format("Expected id of role should be %s, but was %s",
                        ROLE_ID, actual.getId()));
    }

    @Test
    void getRoleByName_ok() {
        roleDao.save(role);
        Optional<Role> actual = roleDao.getRoleByName(USER_ROLE);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(ROLE_ID, actual.get().getId(),
                String.format("Expected id of role should be %s, but was %s",
                        ROLE_ID, actual.get().getId()));
        Assertions.assertEquals(Role.RoleName.USER, actual.get().getRoleName(),
                String.format("Expected role should be %s, but was %s",
                        Role.RoleName.USER, actual.get().getRoleName()));
    }

    @Test
    void getRoleByName_nonExistedRole_notOk() {
        roleDao.save(role);
        Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName("UNKNOWN"),
                "Expected DataProcessingException to be thrown for non-existed role's name");
    }

    @Test
    void getRoleByName_wrongRole_NotOk() {
        roleDao.save(role);
        Assertions.assertThrows(NoSuchElementException.class, () ->
                        roleDao.getRoleByName("ADMIN").get(),
                "Expected NoSuchElementException to be thrown for wrong role");
    }
}
