package ru.kata.spring.boot_security.demo.init;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class InitUserForDataBase {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleServiceImpl roleService;

    public InitUserForDataBase(UserService userService, RoleServiceImpl roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (roleService.findAll().isEmpty()) {
            Role administratorRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");

            Set<Role> rolesAdministrator = new HashSet<>();
            Set<Role> rolesUser = new HashSet<>();
            rolesAdministrator.add(administratorRole);
            rolesUser.add(userRole);
            User administrator = new User("Admin",
                    "Admin",
                    "admin@admin.ru",
                    "admin",
                    rolesAdministrator);
            User user = new User("User",
                    "User",
                    "user@user.ru",
                    "user",
                    rolesUser);
            roleService.add(administratorRole);
            roleService.add(userRole);
            userService.add(administrator);
            userService.add(user);
        }
    }
}
