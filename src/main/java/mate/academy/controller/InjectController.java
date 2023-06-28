package mate.academy.controller;

import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inject")
public class InjectController {
    private final RoleService roleService;

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public InjectController(RoleService roleService,
                            AuthenticationService authenticationService,
                            UserService userService) {
        this.roleService = roleService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping
    public String injectData() {
        roleService.save(new Role(Role.RoleName.ADMIN));
        roleService.save(new Role(Role.RoleName.USER));

        Role roleBob = roleService.getRoleByName("USER");
        User bob = new User();
        bob.setEmail("bob");
        bob.setPassword("1234");
        bob.setRoles(Set.of(roleBob));
        userService.save(bob);

        Role roleAlice = roleService.getRoleByName("ADMIN");
        User alice = new User();
        alice.setEmail("alice");
        alice.setPassword("1234");
        alice.setRoles(Set.of(roleAlice));
        userService.save(alice);
        return "Done!";
    }
}
