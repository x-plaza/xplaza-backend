package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.*;
import com.backend.xplaza.service.CouponService;
import com.backend.xplaza.service.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;
    @Autowired
    private RoleService roleService;
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
    public ResponseEntity<String> getCoupons(@RequestParam(value ="user_id",required = true) @Valid Long user_id) throws JsonProcessingException {
        start = new Date();
        List<CouponList> dtos = null;
        String role_name = roleService.getRoleNameByUserID(user_id);
        if(role_name == null) dtos = null;
        else if(role_name.equals("Master Admin")) { dtos = couponService.listCoupons(); }
        else{ dtos = couponService.listCouponsByUserID(user_id); }
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Coupon List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCouponDetails (@PathVariable @Valid Long id) throws JsonProcessingException {
        start = new Date();
        CouponDetails dtos = couponService.getCouponDetails(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Coupon List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value= "/validate-coupon", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> validateCoupon (@RequestParam(value ="coupon_code",required = true) @Valid String coupon_code,
                                                       @RequestParam(value ="net_order_amount",required = true) @Valid Double net_order_amount,
                                                       @RequestParam(value ="shop_id",required = true) @Valid Long shop_id) throws ParseException {
        start = new Date();
        if(!couponService.checkCouponValidity(coupon_code,net_order_amount,shop_id))
        {
            end = new Date();
            responseTime = end.getTime() - start.getTime();
            return new ResponseEntity<>(new ApiResponse(responseTime, "Validate Coupon", HttpStatus.FORBIDDEN.value(),
                    "Error", "Coupon is not valid!",null), HttpStatus.FORBIDDEN);
        }
        Double coupon_amount = couponService.calculateCouponAmount(coupon_code,net_order_amount);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Validate Coupon", HttpStatus.OK.value(),"Success",
                "Coupon is valid.", coupon_amount.toString()), HttpStatus.OK);
    }

    @PostMapping(value= "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addCoupon (@RequestBody @Valid Coupon coupon) {
        start = new Date();
        // check if same coupon already exists?
        if(couponService.isExist(coupon))
        {
            end = new Date();
            responseTime = end.getTime() - start.getTime();
            return new ResponseEntity<>(new ApiResponse(responseTime, "Add Coupon", HttpStatus.FORBIDDEN.value(),
                    "Error", "Coupon already exists! Please use different coupon code.",null), HttpStatus.FORBIDDEN);
        }
        couponService.addCoupon(coupon);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Coupon", HttpStatus.CREATED.value(),"Success", "Coupon has been created.",null), HttpStatus.CREATED);
    }

    @PutMapping(value= "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateCoupon (@RequestBody @Valid Coupon coupon) {
        start = new Date();
        for (CouponShopLink csl : coupon.getCouponShopLinks()) {
            csl.setCoupon_id(coupon.getId());
        }
        couponService.updateCoupon(coupon);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Coupon", HttpStatus.OK.value(),"Success", "Coupon has been updated.",null), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteCoupon (@PathVariable @Valid Long id) {
        String coupon_name = couponService.getCouponNameByID(id);
        start = new Date();
        couponService.deleteCoupon(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Coupon", HttpStatus.OK.value(),"Success", coupon_name + " has been deleted.",null), HttpStatus.OK);
    }
}
