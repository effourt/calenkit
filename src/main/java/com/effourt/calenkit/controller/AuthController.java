package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Auth;
import com.effourt.calenkit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/")
    public String test() {
        return "index";
    }

    @GetMapping("/test")
    public String test2(Model model) {
        Auth auth = new Auth();
        auth.setAuthRefresh("AAAA");
        auth.setAuthAccess("BBBB");
        Auth savedToken = authService.saveToken(auth);
        model.addAttribute("refresh", savedToken.getAuthRefresh());
        model.addAttribute("access", savedToken.getAuthAccess());
        return "index";
    }
}
