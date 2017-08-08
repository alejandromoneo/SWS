package com.safeline.service;

import com.safeline.entity.Role;
import com.safeline.entity.User;
import com.safeline.entity.UserRole;
import com.safeline.repository.RoleRepository;
import com.safeline.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("userService")
@Transactional
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean checkEmailExists(String email) {
        return (userRepository.findByEmail(email) != null);
    }

    private void encodePassword(User user){
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
    }

    public void registerUser(User user){
        this.encodePassword(user);
        user.addUserRole(roleRepository.findByName("ROLE_USER"));
        this.saveUser(user);
    }

    public User updateUserInformation(User user){
        User userBd = this.getUser(user.getId());

        userBd.setFirstName(user.getFirstName());
        userBd.setLastName(user.getLastName());
        userBd.setCompany(user.getCompany());
        userBd.setPhone(user.getPhone());
        userBd.setEnabled(user.isEnabled());

        return this.saveUser(userBd);
    }

    public void changeUserPassword(User user){
        User userBd = this.getUser(user.getId());
        userBd.setPassword(user.getPassword());
        this.encodePassword(userBd);
        this.saveUser(userBd);
    }

    private User saveUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(Long userId){
        userRepository.delete(userId);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUser(long id) { return userRepository.findOne(id); }

    public String getUserLoggedName(String userEmail){
        User user = userRepository.findByEmail(userEmail);
        return user.getFirstName() + " " + user.getLastName();
    }

    public User addUserRole(Long idUser, String roleName){
        User user = this.getUser(idUser);
        Role role = this.roleRepository.findByName(roleName);
        user.addUserRole(role);
        return user;
    }

    public User deleteUserRole(Long idUser, String roleName){
        User user = this.getUser(idUser);
        user.deleteUserRole(roleName);
        return this.saveUser(user);
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    /* SPRING SECURITY */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user;

        user = userRepository.findByEmail(email);

        if (user == null)
            throw new UsernameNotFoundException("User " + email + " not found");

        List<GrantedAuthority> authorityList = buildAuthorities(user.getUserRoles());
        return buildUser(user, authorityList);
    }

    private org.springframework.security.core.userdetails.User buildUser(User user, List<GrantedAuthority> authorityList){
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(),
                true, true, true, authorityList);
    }

    private List<GrantedAuthority> buildAuthorities (Set<UserRole> userRoles){
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<GrantedAuthority>();

        for(UserRole userRole : userRoles){
            grantedAuthoritySet.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
        }

        return new ArrayList<GrantedAuthority>(grantedAuthoritySet);
    }
    /* END SPRING SECURITY */
}
