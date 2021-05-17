package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.AdminUser;
import com.backend.xplaza.model.AdminUserList;
import com.backend.xplaza.service.AdminUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/adminuser")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;
    private Date start, end;
    long responseTime;

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Set-Cookie", "type=ninja");
    }

    @GetMapping(value = { "", "/" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAdminUsers() throws JsonProcessingException {
        start = new Date();
        List<AdminUserList> dtos = adminUserService.listAdminUsers();
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        /*HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("responseTime", String.valueOf(responseTime));
        responseHeader.set("responseType", "Admin User List");
        responseHeader.set("status", String.valueOf(HttpStatus.OK.value()));
        responseHeader.set("response", "Success");*/
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Admin User List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":"+mapper.writeValueAsString(dtos)+"\n}";
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
                "  \"data\":"+mapper.writeValueAsString(dtos)+"\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value= "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addAdminUser (@RequestBody @Valid AdminUser adminUser) {
        start = new Date();
        adminUserService.addAdminUser(adminUser);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Admin User", HttpStatus.CREATED.value(),"Success", "Admin User has been created.",null), HttpStatus.CREATED);
        //return new ResponseEntity<>(new ApiResponse(true, "Admin User has been created."), HttpStatus.CREATED);
    }

    @PutMapping(value= "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateAdminUser (@RequestBody @Valid AdminUser adminUser) {
        start = new Date();
        adminUserService.updateAdminUser(adminUser);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Admin User", HttpStatus.OK.value(),"Success", "Admin User has been updated.",null), HttpStatus.OK);
        //return new ResponseEntity<>(new ApiResponse(true, "Admin User has been updated."), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteAdminUser (@PathVariable @Valid Long id) {
        String admin_user_name = adminUserService.getAdminUserNameByID(id);
        start = new Date();
        adminUserService.deleteAdminUser(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Admin User", HttpStatus.OK.value(),"Success", admin_user_name + " has been deleted.",null), HttpStatus.OK);
        //return new ResponseEntity<>(new ApiResponse(true, admin_user_name + " has been deleted."), HttpStatus.OK);
    }
}
