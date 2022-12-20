package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleDaoTest extends AbstractTest {
    private static final long NONE_ID = 0L;
    private static final long VALID_FIRST_ID = 1L;
    private static final long VALID_SECOND_ID = 2L;
    private static RoleDao roleDao;
    private static Role userRole;
    private static Role adminRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeAll
    static void setUp() {
        userRole = new Role(Role.RoleName.USER);
        adminRole = new Role(Role.RoleName.ADMIN);
    }

    @BeforeEach
    void beforeEach() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole.setId(NONE_ID);
        userRole.setId(NONE_ID);
    }

    @Test
    void saveOneRole_Ok() {
        Role actual = roleDao.save(userRole);
        assertNotNull(actual, "You must return Role object");
        assertEquals(VALID_FIRST_ID, actual.getId(), "In you Role object you need to add Id");
    }

    @Test
    void saveTwoRoles_Ok() {
        Role firstRole = roleDao.save(userRole);
        Role secondRole = roleDao.save(adminRole);
        assertEquals(VALID_FIRST_ID, firstRole.getId(),
                "First role in saving query must have id 1");
        assertEquals(VALID_SECOND_ID, secondRole.getId(),
                "Second role in saving query must have id 2");
    }

    @Test
    void getRoleByName_Ok() {
        Role expected = roleDao.save(userRole);
        assertEquals(expected.getRoleName(),
                roleDao.getRoleByName(expected.getRoleName().name()).get().getRoleName(),
                "You must return role with the same name");
    }

    @Test
    void getRoleByName_NotOk() {
        assertEquals(Optional.empty(), roleDao.getRoleByName(userRole.getRoleName().name()),
                "You must return empty Optional when can't find name");
    }
}
