package mate.academy.controller;

import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inject")
public class InjectController {
    private final RoleService roleService;

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @Autowired
    public InjectController(RoleService roleService,
                            AuthenticationService authenticationService, UserService userService) {
        this.roleService = roleService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping
    public String injectData() {
        Role admin = roleService.save(new Role(Role.RoleName.ADMIN));
        roleService.save(new Role(Role.RoleName.USER));
        authenticationService.register("bob@i.ua", "password");
        User alice = authenticationService.register("alice@i.ua", "password");
        Set<Role> aliseRoles = alice.getRoles();
        aliseRoles.add(admin);
        alice.setRoles(aliseRoles);
        userService.update(alice);
        return "Done!";
    }
}
