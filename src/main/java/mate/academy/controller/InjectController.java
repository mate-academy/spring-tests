package mate.academy.controller;

import java.util.List;
import mate.academy.model.Role;
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
                            UserService userService, AuthenticationService authenticationService) {
        this.roleService = roleService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String injectData() {
        List<Role> roles = roleService.findAll();
        if (!roles.isEmpty()) {
            return "Injection was completed";
        }
        roleService.save(new Role(Role.RoleName.ADMIN));
        roleService.save(new Role(Role.RoleName.USER));

        authenticationService.register("bob", "1234");
        authenticationService.register("alice", "1234");
        // feel free to save some users for testing
        // hint: you can save users with different roles
        return "Done!";
    }
}
