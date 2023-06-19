package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final Long ROLE_ID = 1L;
    private static final Role.RoleName ROLE_USER = Role.RoleName.USER;
    private static final String ROLE_NAME = "USER";
    private static final Role NULL_ROLE = null;
    private static final String NULL_ROLE_NAME = null;
    private RoleDao underTest;
    private Role role;

    @BeforeEach
    void setUp() {
        underTest = new RoleDaoImpl(getSessionFactory());
        role = new Role(ROLE_USER);
    }

    @Test
    void saveSuccess() {
        Role actual = underTest.save(role);
        assertNotNull(actual);
        assertEquals(ROLE_ID, actual.getId());
    }

    @Test
    void saveException() {
        assertThrows(DataProcessingException.class,
                () -> underTest.save(NULL_ROLE));
    }

    @Test
    void getRoleByNameSuccess() {
        underTest.save(role);
        Optional<Role> actual = underTest.getRoleByName(ROLE_NAME);
        assertNotNull(actual.get());
        assertEquals(ROLE_ID, actual.get().getId());
        assertEquals(Role.RoleName.USER, actual.get().getRoleName());
    }

    @Test
    void getRoleByNameException() {
        assertThrows(DataProcessingException.class,
                () -> underTest.getRoleByName(NULL_ROLE_NAME));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }
}
