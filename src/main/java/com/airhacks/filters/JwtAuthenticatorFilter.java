/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.airhacks.filters;

import com.airhacks.beans.Users;
import com.airhacks.binders.Roles;
import com.airhacks.dtos.AuthenticatedUser;
import com.airhacks.pojos.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author MARMAR Soufiane
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
@Roles
public class JwtAuthenticatorFilter implements ContainerRequestFilter {

    @Inject
    private Users users;
    
    @Inject
    private AuthenticatedUser authenticatedUser;
    
    @Context
    private ResourceInfo resourceInfo;
    
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        
        final Function<String, Response> unauthorizedResponse = message -> Response.status(Response.Status.UNAUTHORIZED)
            .entity(message)
            .type(MediaType.TEXT_PLAIN)
            .build()
        ;
        
        final String token = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        if (token == null || "".equals(token.trim())) {
            containerRequestContext.abortWith(unauthorizedResponse.apply(String.format("%s header absent", HttpHeaders.AUTHORIZATION)));
            return;
        }
        
        try {
            final Jws<Claims> jwsToken = Jwts.parser()
                .setSigningKey(users.getJwtSignInKey())
                .parseClaimsJws(token)
            ;
            
            authenticatedUser.setClaims(jwsToken.getBody());
            authenticatedUser.setUser(users.getUser(jwsToken.getBody().getSubject()).get());
            
            final boolean eligibileForRole = Arrays.stream(getExpectedRoles())
                .anyMatch(authenticatedUser.getUser().getRole()::equals)
            ;
            
            if (!eligibileForRole)
                containerRequestContext.abortWith(unauthorizedResponse.apply("Uneligible role"));
        }catch (final JwtException excpetion) {
            containerRequestContext.abortWith(unauthorizedResponse.apply(excpetion.getMessage()));
        }
        
    }
    
    private Role[] getExpectedRoles() {
        return Optional.<AnnotatedElement>ofNullable(resourceInfo.getResourceMethod())
            .filter(method -> method.isAnnotationPresent(Roles.class))
            .orElseGet(resourceInfo::getResourceClass)
            .getAnnotation(Roles.class)
            .value()
        ;
    }
    
}
