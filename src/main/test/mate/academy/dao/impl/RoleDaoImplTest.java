package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    RoleDao roleDao = new RoleDaoImpl(getSessionFactory());

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    


}