package com.example.gateway.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

@Component
public class Ldap {
    @Value("${ldap.uri}")
    private String ldapUri ;

    @Value("${ldap.baseDn}")
    private String baseDn ;

    @Value("${ldap.context}")
    private String ldapContx ;
    public void login(){
        //TODO paste code in old project to connect to filenet and login
    }



    public  ArrayList<String> getUserGroups(String userId, String pass) {
    // LDAP server details
    String ldapUrl = ldapUri;
    String ldapUser = "CN=" + userId + "," + baseDn; // LDAP bind user DN
    ArrayList<String> userGroups = new ArrayList<>();

    // Set up the environment for creating the initial context
    Properties env = new Properties();
    env.put(Context.INITIAL_CONTEXT_FACTORY, ldapContx);
    env.put(Context.PROVIDER_URL, ldapUrl);
    env.put(Context.SECURITY_PRINCIPAL, userId);
    env.put(Context.SECURITY_CREDENTIALS, pass);

    try {
        // Create the initial context
        DirContext context = new InitialDirContext(env);

        // Create the search controls
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        // Set up the search filter
        String searchFilter = "(cn=" + userId + ")"; // Filter to search for the user

        // Perform the search
        NamingEnumeration<SearchResult> results = context.search(baseDn, searchFilter, searchControls);

        // Retrieve and print the groups of the user
        if (results.hasMore()) {
            SearchResult searchResult = results.next();
            Attributes attributes = searchResult.getAttributes();
            Attribute groupAttribute = attributes.get("memberOf");
            if (groupAttribute != null) {
                NamingEnumeration<?> groupValues = groupAttribute.getAll();
                while (groupValues.hasMore()) {
                    String groubName = groupValues.next().toString();
                    int commaIndex=  groubName.indexOf(',');
                    groubName = groubName.substring(3,commaIndex);
                    userGroups.add(groubName);
                    System.out.println("Group: " + groubName);
                    System.out.println("Comma Index: " + commaIndex);
                }
            }

        }
// Close the context
        context.close();

    } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
    }

    return  userGroups;
}

}


