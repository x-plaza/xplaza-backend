package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.OrderItem;
import com.backend.xplaza.service.OrderItemService;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;
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

    @GetMapping(value = { "", "/" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrderItems() throws JsonProcessingException, JSONException {
        start = new Date();
        List<OrderItem> dtos = orderItemService.listOrderItems();
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Order Item List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getOrderItem(@PathVariable @Valid long id) throws JsonProcessingException {
        start = new Date();
        OrderItem dtos = orderItemService.listOrderItem(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Order Item List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value= "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addOrderItem (@RequestBody @Valid OrderItem orderItem) {
        start = new Date();
        orderItemService.addOrderItem(orderItem);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Order Items", HttpStatus.CREATED.value(),"Success", "Order Item has been created.",null), HttpStatus.CREATED);
    }

    @PutMapping(value= "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateOrderItem (@RequestBody @Valid OrderItem orderItem) {
        start = new Date();
        orderItemService.updateOrderItem(orderItem);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Order Items", HttpStatus.OK.value(),"Success", "Order Item has been updated.",null), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteOrderItem (@PathVariable @Valid long id) {
        String order_item_name = orderItemService.getOrderItemNameByID(id);
        start = new Date();
        orderItemService.deleteOrderItem(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Order Items", HttpStatus.OK.value(),"Success", order_item_name + " has been deleted.",null), HttpStatus.OK);
    }
}
