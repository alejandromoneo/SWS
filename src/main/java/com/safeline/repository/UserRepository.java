package com.safeline.repository;

import com.safeline.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alejandromoneomartinez on 11/7/17.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);
}
