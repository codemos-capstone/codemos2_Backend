package com.seoultech.codemos.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/google")
public class GoogleLoginController {
    @GetMapping("/loginFailure")
    public void LoginFail(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3000/");
    }
}
