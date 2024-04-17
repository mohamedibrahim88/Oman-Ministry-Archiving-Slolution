package com.example.gateway.service;

import com.example.gateway.client.FileNet;
import com.example.gateway.client.Ldap;
import com.filenet.api.core.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Service
//@RequiredArgsConstructor
public class AuthenticationService {

   @Autowired
    Ldap ldap;
    @Autowired
    FileNet fileNet;

    public Connection login(){

        return fileNet.getCEConnection();
    }
    public ArrayList<String> getUserGroups(String userID , String pass)
    {
        return ldap.getUserGroups(userID,pass);
    }
}
