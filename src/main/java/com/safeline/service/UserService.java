package com.safeline.service;

import com.safeline.entity.User;
import com.safeline.entity.UserRole;
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
    private BCryptPasswordEncoder passwordEncoder;

    public boolean checkEmailExists(String email) {
        return (userRepository.findByEmail(email) != null);
    }

    public void registerUser(User user){
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        userRepository.save(user);
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
}
