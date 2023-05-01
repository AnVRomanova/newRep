package com.example31._Spring_Security.service;


import com.example31._Spring_Security.model.Role;
import com.example31._Spring_Security.model.User;

import java.util.List;

public interface UserServiceInterface {
    void add(User user);

    List<User> listUsers();

    User readUser(Long id);

    void edit(User user);

    void delete(Long id);

    List<Role> getSetOfRoles(List<String> rolesId);

    User findByName(String name);


}
