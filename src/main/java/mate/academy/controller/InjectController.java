package mate.academy.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inject")
public class InjectController {
    private final RoleService roleService;
    private final UserService userService;

    public InjectController(RoleService roleService,
                            UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public String injectData() {
        List<Role> roles = roleService.findAll();
        if (!roles.isEmpty()) {
            return "Injection was completed";
        }
        // Save roles
        roleService.save(new Role(Role.RoleName.ADMIN));
        roleService.save(new Role(Role.RoleName.USER));
        roles = roleService.findAll();

        // save users
        User bob = new User();
        bob.setEmail("bob");
        bob.setPassword("1234");
        bob.setRoles(new HashSet<>(roles));
        userService.save(bob);

        User alice = new User();
        alice.setEmail("alice");
        alice.setPassword("1234");
        alice.setRoles(Set.of(roles.get(1)));
        userService.save(alice);

        return "Done!";
    }
}
