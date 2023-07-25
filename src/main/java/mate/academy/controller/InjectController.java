package mate.academy.controller;

import java.util.ArrayList;
import java.util.List;
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
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public InjectController(RoleService roleService,
                            UserService userService,
                            AuthenticationService authenticationService) {
        this.roleService = roleService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String injectData() {
        roleService.save(new Role(Role.RoleName.ADMIN));
        roleService.save(new Role(Role.RoleName.USER));
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.getRoleByName("ADMIN"));
        roles.add(roleService.getRoleByName("USER"));

        authenticationService.register("bob", "1234");

        User alice = new User();
        alice.setEmail("alice@gmail.com");
        alice.setPassword("qwerty");
        alice.setRoles(Set.of(roles.get(0)));
        userService.save(alice);

        User john = new User();
        john.setEmail("john@gmail.com");
        john.setPassword("password");
        john.setRoles(Set.of(roles.get(1)));
        userService.save(john);

        return "Done!";
    }
}
