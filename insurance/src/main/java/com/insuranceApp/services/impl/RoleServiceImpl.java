package com.insuranceApp.services.impl;

import com.insuranceApp.models.Role;
import com.insuranceApp.models.UserEntity;
import com.insuranceApp.repository.RoleRepository;
import com.insuranceApp.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        roleRepository.save(new Role(1, "ADMIN", new ArrayList<UserEntity>()));
        roleRepository.save(new Role(2, "USER", new ArrayList<UserEntity>()));
    }

    @Override
    public Role getRole(String name) {
        return roleRepository.findByName(name);
    }
}
