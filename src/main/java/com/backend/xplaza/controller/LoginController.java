package com.backend.xplaza.controller;

import com.backend.xplaza.model.Login;
import com.backend.xplaza.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/api/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    private Date start, end;
    private long responseTime;

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Set-Cookie", "type=ninja");
    }

    @PostMapping(value = { "", "/" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loginAdminUser (@RequestParam("username") @Valid String username, @RequestParam("password") @Valid String password) throws IOException {
        start = new Date();
        Login dtos = loginService.getAdminUserDetails(username);
        if (dtos != null) {
            boolean isValidUser = loginService.isVaidUser(username, password);
            if(isValidUser) {
                dtos.setAuthentication(true);
            } else {
                dtos.setAuthData(null);
                dtos.setShopList(null);
                dtos.setPermissions(null);
            }
        } else {
            dtos = new Login();
            dtos.setAuthentication(false);
            dtos.setAuthData(null);
            dtos.setShopList(null);
            dtos.setPermissions(null);
        }
        end = new Date();
        responseTime = end.getTime() - start.getTime();

        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Authentication And ACL\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" +
                mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
