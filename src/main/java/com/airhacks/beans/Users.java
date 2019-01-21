/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.airhacks.beans;

import com.airhacks.dtos.UserCredentials;
import com.airhacks.pojos.Role;
import com.airhacks.pojos.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author MARMAR Soufiane
 */
@ApplicationScoped
public class Users {
    
    private Collection<User> users;
    
    @PostConstruct
    public void initialized() {
        
        users = new ArrayList<>();
        
        User customer = new User();
        
        customer.setUserName("customer");
        customer.setPassword("sample");
        customer.setRole(Role.CUSTOMER);
        
        User admin = new User();
        
        admin.setUserName("admin");
        admin.setPassword("nimda");
        admin.setRole(Role.ADMIN);
        
        users.addAll(Arrays.asList(admin, customer));
        
    }
    
    public Optional<User> authenticate(final UserCredentials userCredentials) {
        
        final Predicate<User> hasSameUserNameAndLogin = user -> {
            
            return Objects.equals(userCredentials.getUserName(), user.getUserName()) && Objects.equals(userCredentials.getPassword(), user.getPassword());
            
        };
        
        return users.stream()
            .filter(hasSameUserNameAndLogin)
            .findFirst()
        ;
        
    }
    
    public Optional<User> getUser(final String userName) {
        Predicate<User> hasUserName = user -> Objects.equals(user.getUserName(), userName);
        
        return users.stream()
            .filter(hasUserName)
            .findFirst()
        ;
    }
    
    public String getJwtSignInKey() {
        return "secret";
    }
    
}
