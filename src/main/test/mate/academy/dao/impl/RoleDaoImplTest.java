package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {

        //roleDao = new RoleDaoImpl();
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}