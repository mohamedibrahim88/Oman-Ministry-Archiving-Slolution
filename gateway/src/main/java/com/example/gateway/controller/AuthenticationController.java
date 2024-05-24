package com.example.gateway.controller;

import com.example.gateway.DTOs.GeneralResponse;
import com.example.gateway.client.Ldap;
import com.example.gateway.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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
    public ResponseEntity<?> login(){
        authenticationService.login();
        return new ResponseEntity<>(new GeneralResponse<>("success","200","login success"),HttpStatus.ACCEPTED);
    }

    @GetMapping()
    @ResponseBody
    public ResponseEntity<?> getUserGroups(@RequestParam (name="userId") String userID , @RequestParam (name="pass") String pass) {
        List<String> userGroups = authenticationService.getUserGroups(userID,pass);
        return new ResponseEntity<>(new GeneralResponse<>("success","200",userGroups), HttpStatus.ACCEPTED);
    }
}
