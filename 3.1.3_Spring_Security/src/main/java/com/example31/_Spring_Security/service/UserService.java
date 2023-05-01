package com.example31._Spring_Security.service;


import com.example31._Spring_Security.model.Role;
import com.example31._Spring_Security.model.User;
import com.example31._Spring_Security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Transactional
    @Override
    public void add(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);

    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User readUser(Long id) {
        return userRepository.getOne(id);
    }

    @Transactional
    @Override
    public void edit(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Role> getSetOfRoles(List<String> rolesId) {
        List<Role> roleList = new ArrayList<>();
        for (String id : rolesId) {
            roleList.add(roleService.readRole(Long.parseLong(id)));
        }
        return roleList;
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

}
