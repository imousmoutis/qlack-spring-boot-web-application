package com.eurodyn.qlack.test.web.controller;

import com.eurodyn.qlack.fuse.aaa.annotations.annotation.ResourceAccess;
import com.eurodyn.qlack.fuse.aaa.annotations.annotation.ResourceOperation;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import com.eurodyn.qlack.fuse.security.service.AuthenticationService;
import com.eurodyn.qlack.fuse.security.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author European Dynamics
 */
@Controller
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private LogoutService logoutService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value="/login")
    @ResponseBody
    public Response login(@RequestBody UserDetailsDTO user, HttpServletResponse response) {

        String generatedJwt = authenticationService.authenticate(user);

        response.setHeader(HttpHeaders.AUTHORIZATION, generatedJwt);

        return Response.ok(user).build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/logout")
    @ResponseBody
    public Response logout(HttpServletRequest req){
        logoutService.performLogout(req);

        return Response.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET, value="/app/user")
    @ResponseBody
    @ResourceAccess(roleAccess = {"Administrator"})
    public List<UserDTO> user() {
        List<UserDTO> users = new ArrayList<>();

        users.add(userService.getUserByName("admin"));
        return users;
    }

    @RequestMapping(method = RequestMethod.POST, value="/app/user")
    @ResponseBody
    @ResourceAccess(
            operations = {
                    @ResourceOperation(operation = "CREATE_PERMISSION")
            })
    public String createUser(@RequestBody UserDTO user) {

        return userService.createUser(user);
    }

}
