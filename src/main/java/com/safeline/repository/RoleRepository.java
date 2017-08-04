package com.safeline.repository;

import com.safeline.entity.Role;
import com.safeline.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alejandromoneomartinez on 12/7/17.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Serializable> {

    Role findByName(String name);

}
