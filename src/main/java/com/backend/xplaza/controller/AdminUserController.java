package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.AdminUser;
import com.backend.xplaza.model.AdminUserList;
import com.backend.xplaza.model.AdminUserShopLink;
import com.backend.xplaza.model.ConfirmationToken;
import com.backend.xplaza.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/adminuser")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    private Date start, end;
    private Long responseTime;

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Set-Cookie", "type=ninja");
    }

    @GetMapping(value = { "", "/" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAdminUsers(@RequestParam(value ="user_id",required = true) @Valid Long user_id) throws JsonProcessingException {
        start = new Date();
        ObjectMapper mapper = new ObjectMapper();
        String data = null;
        String role_name = roleService.getRoleNameByUserID(user_id);
        if(role_name == null) data = null;
        else if(role_name.equals("Master Admin")) {
            List<AdminUserList> dtosList = adminUserService.listAdminUsers();
            data = mapper.writeValueAsString(dtosList);
        } else {
            AdminUserList dtos = adminUserService.listAdminUser(user_id);
            data = mapper.writeValueAsString(dtos);
        }
        end = new Date();
        responseTime = end.getTime() - start.getTime();

        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Admin User List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" +  data + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAdminUser(@PathVariable @Valid Long id) throws JsonProcessingException {
        start = new Date();
        AdminUserList dtos = adminUserService.listAdminUser(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Admin User List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value= "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addAdminUser (@RequestBody @Valid AdminUser adminUser) {
        start = new Date();
        AdminUser user = adminUserService.listAdminUser(adminUser.getUser_name());
        if(user != null) {
            end = new Date();
            responseTime = end.getTime() - start.getTime();
            return new ResponseEntity<>(new ApiResponse(responseTime, "Add Admin User", HttpStatus.FORBIDDEN.value(),
                    "Failed", "User Already Exist. ", null), HttpStatus.FORBIDDEN);
        } else {
            // Encrypt Password with Salt
            byte[] byteSalt = null;
            try {
                byteSalt = securityService.getSalt();
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger("Salt error").log(Level.SEVERE, null, ex);
            }
            byte[] biteDigestPsw = securityService.getSaltedHashSHA512(adminUser.getPassword(), byteSalt);
            String strDigestPsw = securityService.toHex(biteDigestPsw);
            String strSalt = securityService.toHex(byteSalt);
            adminUser.setPassword(strDigestPsw);
            adminUser.setSalt(strSalt);
            //--------------------------------------
            adminUserService.addAdminUser(adminUser);
            end = new Date();
            responseTime = end.getTime() - start.getTime();
        }
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Admin User", HttpStatus.CREATED.value(),"Success", "Admin User has been created. " +
                "A confirmation link has been sent to the email. Please confirm it first to activate the account. ",null), HttpStatus.CREATED);
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResponse> confirmUserAccount(@RequestParam("token") String confirmation_token)
    {
        start = new Date();
        ConfirmationToken token = confirmationTokenService.getConfirmationToken(confirmation_token);
        if(token != null) {
            AdminUser adminUser = adminUserService.listAdminUser(token.getAdminUser().getUser_name());
            adminUser.setIs_confirmed(true);
            adminUserService.updateAdminUserConfirmationStatus(adminUser.getId(),adminUser.getIs_confirmed());
        } else {
            end = new Date();
            responseTime = end.getTime() - start.getTime();
            return new ResponseEntity<>(new ApiResponse(responseTime, "Confirm Admin User", HttpStatus.FORBIDDEN.value(),"Error", "The link is either invalid or broken!",null), HttpStatus.FORBIDDEN);
        }
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Confirm Admin User", HttpStatus.OK.value(),"Success", "Admin User has been confirmed successfully. Please Login now.",null), HttpStatus.OK);
    }

    @PostMapping(value= "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> changeAdminUserPassword (@RequestParam("username") @Valid String username,
                                                          @RequestParam("oldPassword") @Valid String oldPassword,
                                                          @RequestParam("newPassword") @Valid String newPassword) throws IOException {
        start = new Date();
        boolean isValidUser = loginService.isVaidUser(username, oldPassword);
        if(!isValidUser) {
            return new ResponseEntity<>(new ApiResponse(responseTime, "Change Admin User Password", HttpStatus.FORBIDDEN.value(),"Failure", "Old Password does not match.",null), HttpStatus.FORBIDDEN);
        }
        byte[] byteSalt = null;
        try {
            byteSalt = securityService.getSalt();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger("Salt error").log(Level.SEVERE, null, ex);
        }
        byte[] biteDigestPsw = securityService.getSaltedHashSHA512(newPassword, byteSalt);
        String strDigestPsw = securityService.toHex(biteDigestPsw);
        String strSalt = securityService.toHex(byteSalt);
        adminUserService.changeAdminUserPassword(strDigestPsw,strSalt,username);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Change Admin User Password", HttpStatus.OK.value(),"Success", "Password has been updated successfully.",null), HttpStatus.OK);
    }

    @PutMapping(value= "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateAdminUser (@RequestBody @Valid AdminUser adminUser) {
        start = new Date();
        for (AdminUserShopLink ausl : adminUser.getAdminUserShopLinks()) {
            ausl.setId(adminUser.getId());
        }
        adminUserService.updateAdminUser(adminUser);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Admin User", HttpStatus.OK.value(),"Success", "Admin User has been updated.",null), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteAdminUser (@PathVariable @Valid Long id) {
        String admin_user_name = adminUserService.getAdminUserNameByID(id);
        start = new Date();
        adminUserService.deleteAdminUser(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Admin User", HttpStatus.OK.value(),"Success", admin_user_name + " has been deleted.",null), HttpStatus.OK);
    }
}
