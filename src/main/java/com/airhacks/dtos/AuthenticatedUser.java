/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.airhacks.dtos;

import com.airhacks.pojos.User;
import io.jsonwebtoken.Claims;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author MARMAR Soufiane
 */
@RequestScoped
public class AuthenticatedUser {
    
    private Claims claims;
    private User user;

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
