package com.cy.person_blog.controller;

import com.cy.person_blog.entity.User;
import com.cy.person_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(User user,
                             @RequestParam String confirm,
                             Model model) {
        boolean ok = userService.register(user, confirm);
        if (!ok) {
            model.addAttribute("error", "注册失败：密码不一致或邮箱已被占用");
            return "register";
        }
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        var userOpt = userService.authenticate(email, password);
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }

        session.setAttribute("currentUser", userOpt.get());
        return "redirect:/";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }



}