package com.eurodyn.qlack.test.web.controller;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceAccess;
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceOperation;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.service.OperationService;
import com.eurodyn.qlack.fuse.aaa.service.UserGroupService;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import com.eurodyn.qlack.util.jwt.dto.JwtGenerateRequestDTO;
import com.eurodyn.qlack.util.jwt.service.JwtService;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author European Dynamics
 */
@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final JwtService jwtService;
  private final UserGroupService userGroupService;
  private final OperationService operationService;

  @RequestMapping(method = RequestMethod.POST, value = "/login")
  @ResponseBody
  public Response login(@RequestBody UserDetailsDTO user, HttpServletResponse response) {

    String userId = userService.canAuthenticate(user.getUsername(), user.getPassword());
    Collection<String> authorities = getAuthorities(userId);

    JwtGenerateRequestDTO dto = JwtGenerateRequestDTO.builder()
        .subject(user.getUsername()).authorities(authorities).build();
    String generatedJwt = jwtService.generateJwt(dto);
    response.setHeader(HttpHeaders.AUTHORIZATION, generatedJwt);

    return Response.ok(user).build();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/app/user")
  @ResponseBody
  @ResourceAccess(roleAccess = {"Administrator"})
  public List<UserDTO> user() {
    List<UserDTO> users = new ArrayList<>();

    users.add(userService.getUserByName("admin"));
    return users;
  }

  @RequestMapping(method = RequestMethod.POST, value = "/app/user")
  @ResponseBody
  @ResourceAccess(
      operations = {
          @ResourceOperation(operation = "CREATE_PERMISSION")
      })
  public String createUser(@RequestBody UserDTO user) {

    return userService.createUser(user, null);
  }

  private Collection<String> getAuthorities(String userId) {
    Set<String> userGroupsIds = userGroupService.getUserGroupsIds(userId);
    Collection<String> authorities =  userGroupService.getGroupsByID(userGroupsIds, false)
        .stream().map(UserGroupDTO::getName).collect(Collectors.toList());

    Set<String> operations = operationService.getPermittedOperationsForUser(userId, false);

    authorities.addAll(operations);
    return authorities;
  }

}
