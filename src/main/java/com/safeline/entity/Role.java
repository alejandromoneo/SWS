package com.safeline.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alejandromoneomartinez on 11/7/17.
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<>();

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", userRoles=" + userRoles +
                '}';
    }
}
