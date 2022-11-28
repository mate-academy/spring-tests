package mate.academy.service;

import java.util.NoSuchElementException;
import mate.academy.model.Role;

public interface RoleService {
    Role save(Role role);

    Role getRoleByName(String roleName);
}
