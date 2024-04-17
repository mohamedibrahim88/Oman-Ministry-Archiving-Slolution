package com.example.gateway.controller;

import com.example.gateway.client.Ldap;
import com.example.gateway.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static org.springframework.web.servlet.function.ServerResponse.ok;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    //private final Ldap ldap;

    public AuthenticationController(AuthenticationService authenticationService, Ldap ldap) {
        this.authenticationService = authenticationService;
      //  this.ldap = ldap;
    }

    @PostMapping()
    public void login(){
        authenticationService.login();
    }

    @GetMapping()
    @ResponseBody
    public  ArrayList<String> getUserGroups(@RequestParam (name="userId") String userID , @RequestParam (name="pass") String pass)
    {
        return authenticationService.getUserGroups(userID,pass);
    }
}
