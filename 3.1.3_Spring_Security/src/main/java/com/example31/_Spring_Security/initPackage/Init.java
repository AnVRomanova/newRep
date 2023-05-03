package com.example31._Spring_Security.initPackage;

import com.example31._Spring_Security.model.Role;
import com.example31._Spring_Security.model.User;
import com.example31._Spring_Security.repository.UserRepository;
import com.example31._Spring_Security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Init implements CommandLineRunner {

    private final UserRepository userRepo;
    private final RoleService roleService;

    private final BCryptPasswordEncoder encoder;


    @Autowired
    public Init(BCryptPasswordEncoder encoder, RoleService roleService, UserRepository userRepository) {

        this.userRepo = userRepository;
        this.roleService = roleService;
        this.encoder = encoder;

    }


    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = roleService.listRoles();

        User user = new User();
        user.setEmail("mail@gmail.com");
        user.setPassword(encoder.encode("1234"));
        user.setName("Mike");
        user.setActive(true);
        user.setLastName("LastName2");
        user.addRole(roles.get(0));

        if (userRepo.findByName("Mike") == null) {
            User savedUser = userRepo.save(user);
        }

        User admin = new User();
        admin.setEmail("adminMail@gmail.com");
        admin.setPassword(encoder.encode("1234"));
        admin.setName("admin2");
        admin.setActive(true);
        admin.setLastName("LastName2");
        admin.addRole(roles.get(1));

        if (userRepo.findByName(admin.getName()) == null) {
            User savedAdmin = userRepo.save(admin);
        }

    }
}
