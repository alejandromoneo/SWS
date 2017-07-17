package com.safeline.webController;

import com.safeline.entity.User;
import com.safeline.entity.UserRole;
import com.safeline.service.RoleService;
import com.safeline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
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

        return "signup";
    }

    @PostMapping("/signup")
    public String signupPost(@Valid User user, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "signup";
        }
        if(userService.checkEmailExists(user.getEmail()))  {
            model.addAttribute("emailExists", true);
            return "signup";
        }
        else {
            Set<UserRole> userRoles = new HashSet<>();
            userRoles.add(new UserRole(user, roleService.findByName("ROLE_USER")));
            user.setUserRoles(userRoles);

            userService.registerUser(user);

            return "redirect:/login";
        }
    }
}
