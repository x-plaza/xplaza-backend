package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.*;
import com.backend.xplaza.service.OrderService;
import com.backend.xplaza.service.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
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
    public ResponseEntity<String> getOrdersByAdmin (@RequestParam(value ="user_id",required = true) @Valid Long admin_user_id,
                                            @RequestParam(value ="status",required = false) @Valid String status,
                                            @RequestParam(value ="order_date",required = false) @Valid String order_date)
                                            throws JsonProcessingException, JSONException, ParseException {
        start = new Date();
        List<OrderList> dtos;
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        String role_name = roleService.getRoleNameByUserID(admin_user_id);
        if(role_name == null) dtos = null;
        else if(role_name.equals("Master Admin")) {
            if (status == null && order_date == null) dtos = orderService.listOrdersByAdmin();
            else if (status!=null && order_date == null) dtos = orderService.listOrdersByStatusByAdmin(status);
            else {
                if (status == null) status = "Pending";
                Date date = formatter.parse(order_date);
                dtos = orderService.listOrdersByFilterByAdmin(status,date);
            }
        }
        else{
            if (status == null && order_date == null) dtos = orderService.listOrdersByAdminUserID(admin_user_id);
            else if (status!=null && order_date == null) dtos = orderService.listOrdersByStatusAndAdminUserID(status,admin_user_id);
            else {
                if (status == null) status = "Pending";
                Date date = formatter.parse(order_date);
                dtos = orderService.listOrdersByFilterAndAdminUserID(status,date,admin_user_id);
            }
        }
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Order List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrderDetailsByAdmin (@PathVariable @Valid Long id) throws JsonProcessingException {
        start = new Date();
        OrderDetails dtos = orderService.listOrderDetailsByAdmin(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Order Details\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = { "/by-customer" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrdersByCustomer (@RequestParam(value ="customer_id",required = true) @Valid Long customer_id,
                                                    @RequestParam(value ="status",required = false) @Valid String status,
                                                    @RequestParam(value ="order_date",required = false) @Valid String order_date)
            throws JsonProcessingException, JSONException, ParseException {
        start = new Date();
        List<OrderPlaceList> dtos = null;
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        if (status == null && order_date == null) dtos = orderService.listOrdersByCustomer(customer_id);
        else if (status!=null && order_date == null) dtos = orderService.listOrdersByStatusByCustomer(customer_id,status);
        else if (status!=null && order_date != null)
        {
            Date date = formatter.parse(order_date);
            dtos = orderService.listOrdersByFilterByCustomer(customer_id,status,date);
        }
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Order List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/by-customer/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrderDetailsByCustomer (@PathVariable @Valid Long id) throws JsonProcessingException {
        start = new Date();
        OrderDetails dtos = orderService.listOrderDetailsByAdmin(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Order Details\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value= "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addOrder (@RequestBody @Valid OrderPlace order) throws ParseException {
        start = new Date();

        // Check product availability
        ProductInventory productInventory = orderService.checkProductAvailability(order);
        if(!productInventory.getIs_available())
        {
            end = new Date();
            responseTime = end.getTime() - start.getTime();
            return new ResponseEntity<>(new ApiResponse(responseTime, "Add Order", HttpStatus.FORBIDDEN.value()
                    ,"Error", "Sorry! The item " + productInventory.getName() + " has "+ productInventory.getMax_available_quantity()+" unit(s) left. " +
                    "Please update your order accordingly.",null), HttpStatus.FORBIDDEN);
        }

        // Validate Coupon
        if(order.getCoupon_id() != null) {
            if (!orderService.checkCouponValidity(order,"id"))
            {
                end = new Date();
                responseTime = end.getTime() - start.getTime();
                return new ResponseEntity<>(new ApiResponse(responseTime, "Add Order", HttpStatus.FORBIDDEN.value(),"Error", "Coupon is not valid!",null), HttpStatus.FORBIDDEN);
            }
        }
        else if(order.getCoupon_id() != null) {
            if (!orderService.checkCouponValidity(order,"code"))
            {
                end = new Date();
                responseTime = end.getTime() - start.getTime();
                return new ResponseEntity<>(new ApiResponse(responseTime, "Add Order", HttpStatus.FORBIDDEN.value(),"Error", "Coupon is not valid!",null), HttpStatus.FORBIDDEN);
            }
        }

        // Set Order Prices
        order = orderService.setOrderPrices(order);

        // Place Order
        orderService.addOrder(order);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Order", HttpStatus.CREATED.value(),"Success", "Order has been created.",null), HttpStatus.CREATED);
    }

    /*@PutMapping(value= "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateOrder (@RequestBody @Valid OrderPlace order) {
        start = new Date();
        orderService.updateOrder(order);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Order", HttpStatus.OK.value(),"Success", "Order has been updated.",null), HttpStatus.OK);
    }*/

    @PutMapping(value= "/status-update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateOrderStatus (@RequestParam("invoice_number") @Valid Long invoice_number, @RequestParam("status") @Valid Long status,
                                                          @RequestParam(value="remarks",required = false) @Valid String remarks) {
        start = new Date();
        orderService.updateOrderStatus(invoice_number,remarks,status);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Order Status", HttpStatus.OK.value(),"Success", "Order Status has been updated.",null), HttpStatus.OK);
    }

   /*@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteOrder (@PathVariable @Valid Long id) {
        start = new Date();
        orderService.deleteOrder(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Order", HttpStatus.OK.value(),"Success", "Order no: " + id + " has been deleted.",null), HttpStatus.OK);
    }*/
}
