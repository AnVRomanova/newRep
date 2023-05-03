package com.example31._Spring_Security.service;


import com.example31._Spring_Security.model.Role;

import java.util.List;

public interface RoleServiceInterface {

    List<Role> listRoles();

    Role readRole(Long id);

}
