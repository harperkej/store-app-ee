package com.arberkuci.storeapp.web.api.user;

import com.arberkuci.storeapp.common.logging.facade.LoggingInterceptor;
import com.arberkuci.storeapp.ejb.user.api.UserFacade;
import com.arberkuci.storeapp.common.rest.response.UserDto;
import com.arberkuci.storeapp.web.util.Request;
import com.arberkuci.storeapp.web.util.ResponseBuilder;

import javax.inject.Inject;

import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path(value = "customers")
@Interceptors({LoggingInterceptor.class})
public class UserRestResource {

    @Inject
    UserFacade userFacade;

    @Inject
    ResponseBuilder responseBuilder;

    private final String className = this.getClass().getName();

    private Logger logger = Logger.getLogger(className);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response storeUser(UserDto userDto) {
        Response response;
        try {
            UserDto storedUser = userFacade.storeUser(userDto);
            response = responseBuilder.buildResponseWithListOfResources(storedUser, Request.POST);
        } catch (Exception e) {
            logger.severe("An error occured while storing the user. The exception is -> " + e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build();
        }
        logger.fine("Final response -> " + response);
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response findUserById(@PathParam("id") Long id) {
        Response response;
        try {
            UserDto foundUser = userFacade.findByUserById(id);
            response = this.responseBuilder.buildResponseWithListOfResources(foundUser, Request.GET);
        } catch (Exception e) {
            logger.severe("An error occured while searching the user with id " + id + ". The message is -> " + e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        logger.fine("Final response -> " + response);
        return response;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(UserDto userDto) {
        Response response;
        try {
            UserDto updatedUser = this.userFacade.updateUser(userDto);
            response = this.responseBuilder.buildResponseWithListOfResources(updatedUser, Request.UPDATE);
        } catch (Exception e) {
            logger.severe("An error occurred while trying to update the user with id " + userDto.getId() + ". The message => " + e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        logger.fine("Final response => " + response);
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("username/{username}")
    public Response findUserByUsername(@PathParam("username") String userName) {
        Response response;
        try {
            UserDto userDto = this.userFacade.getUserByUserName(userName);
            response = this.responseBuilder.buildResponseWithListOfResources(userDto, Request.GET);
        } catch (Exception e) {
            logger.severe("An error occurred while trying to find user by user name -> " + userName);
            response = Response.status(Response.Status.NOT_FOUND).encoding(e.getMessage()).build();
        }
        logger.fine("Final response => " + response);
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        Response response;
        try {
            List<UserDto> foundUsers = userFacade.findAllUsers();
            response = this.responseBuilder.buildResponseWithListOfResources(foundUsers, Request.GET);
        } catch (Exception e) {
            logger.fine("An error occurred while fetching all the users. The message -> " + e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        logger.fine("Final response -> " + response);
        return response;
    }

}
