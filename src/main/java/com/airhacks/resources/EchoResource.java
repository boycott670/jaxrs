/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.airhacks.resources;

import com.airhacks.binders.Roles;
import com.airhacks.dtos.AuthenticatedUser;
import com.airhacks.pojos.Role;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author MARMAR Soufiane
 */
@Path("echo")
@Roles(Role.CUSTOMER)
public class EchoResource {
    
    @Inject
    private AuthenticatedUser authenticatedUser;
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String echo() {
        
        return authenticatedUser.getUser().getUserName();
        
    }
    
}
