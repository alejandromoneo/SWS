package com.safeline.webController;

import com.safeline.entity.Role;
import com.safeline.entity.User;
import com.safeline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by alejandromoneomartinez on 11/7/17.
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping({"/","/index"})
    public String index(){
        return "index";
    }

    @GetMapping("/signin")
    public String signIn(){
        return "signIn";
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
            userService.registerUser(user);
            return "redirect:/signin";
        }
    }

    @GetMapping("/userManagement")
    public String users(Principal principal, Model model){

        model.addAttribute("userLoggedName", userService.getUserLoggedName(principal.getName()));

        model.addAttribute("userList", userService.getAllUsers());

        return "userManagement";
    }

    @GetMapping("/ajax/userManagement/getUsers")
    public String getUsers(Model model) {

        model.addAttribute("userList", userService.getAllUsers());

        return "userManagement :: userDataTable";
    }

    @GetMapping("/ajax/userManagement/addUser")
    public String addUser(Model model){

        User user = new User();

        model.addAttribute("user", user);

        return "userManagement :: userAddForm";
    }

    @PostMapping("/ajax/userManagement/addUser")
    public String addUserPost(@Validated(User.GroupSignUp.class) User user, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "userManagement :: userAddForm";
        }
        if(userService.checkEmailExists(user.getEmail()))  {
            model.addAttribute("emailExists", true);
            return "userManagement :: userAddForm";
        }
        else {
            userService.registerUser(user);
            model.addAttribute("roleList", userService.getAllRoles());
            return "userManagement :: userForm";
        }
    }

    @GetMapping("/ajax/userManagement/getUser/{idUser}")
    public String getUser(@PathVariable("idUser") Long idUser, Model model){

        model.addAttribute("user", userService.getUser(idUser));
        model.addAttribute("roleList", userService.getAllRoles());

        return "userManagement :: userForm";
    }

    @PostMapping("/ajax/userManagement/deleteUser")
    public String deleteUser(@RequestParam Long userId, Model model){

        userService.deleteUser(userId);

        model.addAttribute("userList", userService.getAllUsers());

        return "userManagement :: userDataTable";
    }

    @PostMapping("/ajax/userManagement/updateUserInformation")
    public String updateUser(@Validated(User.GroupUpdate.class) User user, Errors errors, Model model){
        if (errors.hasErrors())
            return "userManagement :: userInformationForm";

        model.addAttribute("user", userService.updateUserInformation(user));

        return "userManagement :: userInformationForm";
    }

    @PostMapping("/ajax/userManagement/changeUserPassword")
    public String usersPost(@Validated(User.GroupChangePassword.class) User user, Errors errors, Model model){
        if (errors.hasErrors())
            return "userManagement :: userPasswordForm";

        userService.changeUserPassword(user);

        return "userManagement :: userPasswordForm";
    }

    @PostMapping("/ajax/userManagement/addUserRole")
    public String addUserRole(@RequestParam Long userId, @RequestParam String roleName, Model model){ //@RequestParam lo utilizaremos cuando necesitemos valores primitivos, ya que con el @ModelAtribute necesitamos el constructor vacio, solo lo usaremos con objectos nuestros

        model.addAttribute("user", userService.addUserRole(userId, roleName));
        model.addAttribute("roleList", userService.getAllRoles());

        return "userManagement :: userRoleForm";
    }

    @PostMapping("/ajax/userManagement/deleteUserRole")
    public String deleteUserRole(@RequestParam Long userId, @RequestParam String roleName, Model model){ //@RequestParam lo utilizaremos cuando necesitemos valores primitivos, ya que con el @ModelAtribute necesitamos el constructor vacio, solo lo usaremos con objectos nuestros

        model.addAttribute("user", userService.deleteUserRole(userId, roleName));
        model.addAttribute("roleList", userService.getAllRoles());

        return "userManagement :: userRoleForm";
    }
}
