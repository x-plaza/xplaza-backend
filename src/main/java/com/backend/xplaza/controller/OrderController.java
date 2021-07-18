package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.Order;
import com.backend.xplaza.model.OrderDetails;
import com.backend.xplaza.model.OrderList;
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
    public ResponseEntity<String> getOrders(@RequestParam(value ="user_id",required = true) @Valid Long user_id,
                                            @RequestParam(value ="status",required = false) @Valid String status,
                                            @RequestParam(value ="order_date",required = false) @Valid String order_date)
                                            throws JsonProcessingException, JSONException, ParseException {
        start = new Date();
        List<OrderList> dtos;
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        String role_name = roleService.getRoleNameByUserID(user_id);
        if(role_name == null) dtos = null;
        else if(role_name.equals("Master Admin")) {
            if (status == null && order_date == null) dtos = orderService.listOrders();
            else if (status!=null && order_date == null) dtos = orderService.listOrdersByStatus(status);
            else {
                if (status == null) status = "Pending";
                Date date = formatter.parse(order_date);
                dtos = orderService.listOrdersByFilter(status,date);
            }
        }
        else{
            if (status == null && order_date == null) dtos = orderService.listOrdersByUserID(user_id);
            else if (status!=null && order_date == null) dtos = orderService.listOrdersByStatusAndUserID(status,user_id);
            else {
                if (status == null) status = "Pending";
                Date date = formatter.parse(order_date);
                dtos = orderService.listOrdersByFilterAndUserID(status,date,user_id);
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
    public ResponseEntity<String> getOrderDetails(@PathVariable @Valid Long id) throws JsonProcessingException {
        start = new Date();
        OrderDetails dtos = orderService.listOrderDetails(id);
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
    public ResponseEntity<ApiResponse> addOrder (@RequestBody @Valid Order order) {
        start = new Date();
        orderService.addOrder(order);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Order", HttpStatus.CREATED.value(),"Success", "Order has been created.",null), HttpStatus.CREATED);
    }

    @PutMapping(value= "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateOrder (@RequestBody @Valid Order order) {
        start = new Date();
        orderService.updateOrder(order);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Order", HttpStatus.OK.value(),"Success", "Order has been updated.",null), HttpStatus.OK);
    }

    @PutMapping(value= "/status-update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateOrderStatus (@RequestParam("invoice_number") @Valid Long invoice_number, @RequestParam("status") @Valid Long status,
                                                          @RequestParam(value="remarks",required = false) @Valid String remarks) {
        start = new Date();
        orderService.updateOrderStatus(invoice_number,remarks,status);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Order Status", HttpStatus.OK.value(),"Success", "Order Status has been updated.",null), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteOrder (@PathVariable @Valid Long id) {
        start = new Date();
        orderService.deleteOrder(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Order", HttpStatus.OK.value(),"Success", "Order no: " + id + " has been deleted.",null), HttpStatus.OK);
    }
}
