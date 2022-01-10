package mate.academy.dao.impl;

import mate.academy.dao.AbstractTest;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class RoleDaoImplTest extends AbstractTest {
    private RoleDaoImpl roleDao;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void getRoleByName_DataProcessingException() {
        try {
            roleDao.getRoleByName("TEST");
        } catch (DataProcessingException e) {
            assertEquals("Couldn't get role by role name: TEST", e.getMessage());
            return;
        }
        fail();
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] { Role.class };
    }
}
