/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.airhacks.resources;

import com.airhacks.beans.Users;
import com.airhacks.dtos.UserCredentials;
import com.airhacks.pojos.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author MARMAR Soufiane
 */
@Path("login")
public class LoginResource {
    
    @Inject
    private Users users;
    
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(final UserCredentials userCredentials) {
        
        final Optional<User> authenticatingUser = users.authenticate(userCredentials);
        
        if (!authenticatingUser.isPresent()) {
            
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Wrong credentials")
                .build()
            ;
            
        }
        
        return Response.status(Response.Status.OK)
            .entity(
                Jwts.builder()
                    .setSubject(authenticatingUser.get().getUserName())
                    .signWith(SignatureAlgorithm.HS256, users.getJwtSignInKey())
                    .compact()
            )
            .build()
        ;
        
    }
    
}
