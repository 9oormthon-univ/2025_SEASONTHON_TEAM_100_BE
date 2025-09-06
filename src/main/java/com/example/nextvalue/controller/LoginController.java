package com.example.nextvalue.controller;

import com.example.nextvalue.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "oauth_login/login";
    }

    @GetMapping("/")
    public String index(@SessionAttribute(name = "loginMember", required = false) Member loginMember, Model model) {
       model.addAttribute("user", loginMember);
        return "oauth_login/welcome";
    }
}
