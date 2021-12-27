package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.DeliveryCost;
import com.backend.xplaza.model.DeliveryCostList;
import com.backend.xplaza.service.DeliveryCostService;
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
@RequestMapping("/api/delivery-cost")
public class DeliveryCostController {
    @Autowired
    private DeliveryCostService deliveryCostService;
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
    public ResponseEntity<String> getDeliveryCosts() throws JsonProcessingException, JSONException {
        start = new Date();
        List<DeliveryCostList> dtos = deliveryCostService.listDeliveryCosts();
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Delivery Cost List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getDeliveryCost(@PathVariable @Valid Long id) throws JsonProcessingException {
        start = new Date();
        DeliveryCostList dtos = deliveryCostService.listDeliveryCost(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Delivery Cost List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value= "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addDeliveryCost (@RequestBody @Valid DeliveryCost deliveryCost) {
        start = new Date();
        deliveryCostService.addDeliveryCost(deliveryCost);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Delivery Cost", HttpStatus.CREATED.value(),
                "Success", "Delivery Cost has been created.",null), HttpStatus.CREATED);
    }

    @PutMapping(value= "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateDeliveryCostd (@RequestBody @Valid DeliveryCost deliveryCost) {
        start = new Date();
        deliveryCostService.updateDeliveryCost(deliveryCost);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Delivery Cost", HttpStatus.OK.value(),
                "Success", "Delivery Cost has been updated.",null), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteDeliveryCost (@PathVariable @Valid Long id) {
        String delivery_slab = deliveryCostService.getDeliverySlabRangeNameByID(id);
        start = new Date();
        deliveryCostService.deleteDeliveryCost(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Delivery Cost", HttpStatus.OK.value(),
                "Success", "Delivery cost of "+ delivery_slab + " order range has been deleted.",null), HttpStatus.OK);
    }
}
