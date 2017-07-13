package com.safeline.service;

import com.safeline.entity.Role;
import com.safeline.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by alejandromoneomartinez on 12/7/17.
 */
@Service("roleService")
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByName(String name){
        return roleRepository.findByName(name);
    }

    public void save(List<Role> roleList){
        roleRepository.save(roleList);
    }
}
