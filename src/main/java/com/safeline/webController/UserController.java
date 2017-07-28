package com.safeline.webController;

import com.safeline.entity.User;
import com.safeline.entity.UserRole;
import com.safeline.service.RoleService;
import com.safeline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alejandromoneomartinez on 11/7/17.
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @GetMapping({"/","/index"})
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/signup")
    public String signUp(Model model){
        User user = new User();

        model.addAttribute("user", user);

        return "signUp";
    }

    @PostMapping("/signup")
    public String signupPost(@Validated(User.GroupSignUp.class) User user, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "signUp";
        }
        if(userService.checkEmailExists(user.getEmail()))  {
            model.addAttribute("emailExists", true);
            return "signUp";
        }
        else {
            Set<UserRole> userRoles = new HashSet<>();
            userRoles.add(new UserRole(user, roleService.findByName("ROLE_USER")));
            user.setUserRoles(userRoles);

            userService.encodePassword(user);
            userService.saveUser(user);

            return "redirect:/login";
        }
    }

    @GetMapping("/users")
    public String users(Model model){
        List<User> userList = userService.getAllUsers();

        model.addAttribute("userList", userList);

        return "userManagement";
    }

    @GetMapping("/ajax/users/getUsers")
    public String getUsers(Model model){
        List<User> userList = userService.getAllUsers();

        model.addAttribute("userList", userList);

        return "userManagement :: userDataTable";
    }

    @GetMapping("/ajax/users/getUser/{idUser}")
    public String getUser(@PathVariable("idUser") Long idUser, Model model){
        User user = userService.getUser(idUser);

        model.addAttribute("user", user);

        return "userManagement :: formUser";
    }

    @PostMapping("/ajax/users/updateUser")
    public String updateUser(@Validated(User.GroupUpdate.class) User user, Errors errors, Model model){
        if (errors.hasErrors()) {
            return "userManagement :: formUser";
        }

        User userBd = userService.getUser(user.getId());

        userBd.setFirstName(user.getFirstName());
        userBd.setLastName(user.getLastName());
        userBd.setCompany(user.getCompany());
        userBd.setPhone(user.getPhone());

        model.addAttribute("user", userBd);

        userService.saveUser(userBd);

        return "userManagement :: formUser";
    }

    @PostMapping("/ajax/users/changeUserPassword")
    public String usersPost(@Validated(User.GroupChangePassword.class) User user, Errors errors, Model model){
        if (errors.hasErrors())
            return "userManagement :: formUser";

        User userBd = userService.getUser(user.getId());

        userBd.setPassword(user.getPassword());
        userService.encodePassword(userBd);

        userService.saveUser(userBd);

        return "userManagement :: formUser";
    }
}
