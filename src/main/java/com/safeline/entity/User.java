package com.safeline.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alejandromoneomartinez on 11/7/17.
 */

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(length = 30, nullable = false)
    @NotBlank(groups = {GroupSignUp.class, GroupUpdate.class})
    @Size(min=2, max=30, groups = {GroupSignUp.class, GroupUpdate.class})
    private String firstName;

    @Column(length = 60, nullable = false)
    @NotBlank(groups = {GroupSignUp.class, GroupUpdate.class})
    @Size(min=2, max=60, groups = {GroupSignUp.class, GroupUpdate.class})
    private String lastName;

    @Column(length = 60, nullable = false, unique = true)
    @NotBlank(groups = GroupSignUp.class)
    @Email(groups = GroupSignUp.class)
    private String email;

    @Column(nullable = false, length = 60)
    @NotBlank(groups = {GroupSignUp.class, GroupChangePassword.class})
    @Size(min=8, max=60, groups = {GroupSignUp.class, GroupChangePassword.class})
    private String password;

    @Column(length = 45)
    @Size(min=0, max=45, groups = {GroupSignUp.class, GroupUpdate.class})
    private String company;

    @Column(length = 15, nullable = false)
    @NotBlank(groups = {GroupSignUp.class, GroupUpdate.class})
    @Size(min=5, max=15, groups = {GroupSignUp.class, GroupUpdate.class})
    private String phone;

    @Column(nullable = false)
    private boolean enabled = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, String company, String phone, boolean enabled, Set<UserRole> userRoles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.company = company;
        this.phone = phone;
        this.enabled = enabled;
        this.userRoles = userRoles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        return email != null ? email.equals(user.email) : user.email == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", company='" + company + '\'' +
                ", phone='" + phone + '\'' +
                ", enabled=" + enabled + '}';
    }

    public interface GroupSignUp {}

    public interface GroupUpdate {}

    public interface GroupChangePassword {}

}
