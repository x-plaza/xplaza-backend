package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.AdminUser;
import com.backend.xplaza.service.AdminUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping(value = { "", "/" })
    public ResponseEntity<String> getBrands() throws JsonProcessingException {
        start = new Date();
        List<AdminUser> dtos = adminUserService.listAdminUsers();
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

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addBrand (@RequestBody @Valid AdminUser adminUser) {
        adminUserService.addAdminUser(adminUser);
        return new ResponseEntity<>(new ApiResponse(true, "Admin User has been created."), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateBrand (@RequestBody @Valid AdminUser adminUser) {
        adminUserService.updateAdminUser(adminUser);
        return new ResponseEntity<>(new ApiResponse(true, "Admin User has been updated."), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBrand (@PathVariable @Valid Long id) {
        String admin_user_name = adminUserService.getAdminUserNameByID(id);
        adminUserService.deleteAdminUser(id);
        return new ResponseEntity<>(new ApiResponse(true, admin_user_name + " has been deleted."), HttpStatus.OK);
    }
}
