package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.Currency;
import com.backend.xplaza.service.CurrencyService;
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
@RequestMapping("/api/currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;
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
    public ResponseEntity<String> getCurrencies() throws JsonProcessingException, JSONException {
        start = new Date();
        List<Currency> dtos = currencyService.listCurrencies();
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Currency List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":"+mapper.writeValueAsString(dtos)+"\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCurrency(@PathVariable @Valid Long id) throws JsonProcessingException {
        start = new Date();
        Currency dtos = currencyService.listCurrency(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Currency List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":"+mapper.writeValueAsString(dtos)+"\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCurrency (@RequestBody @Valid Currency brand) {
        start = new Date();
        currencyService.addCurrency(brand);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Currency", HttpStatus.CREATED.value(),"Success", "Currency has been created.",null), HttpStatus.CREATED);
        //return new ResponseEntity<>(new ApiResponse(true, "Currency has been created."), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCurrency (@RequestBody @Valid Currency brand) {
        start = new Date();
        currencyService.updateCurrency(brand);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Currency", HttpStatus.OK.value(),"Success", "Currency has been updated.",null), HttpStatus.OK);
        //return new ResponseEntity<>(new ApiResponse(true, "Currency has been updated."), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCurrency (@PathVariable @Valid Long id) {
        String currency_name = currencyService.getCurrencyNameByID(id);
        start = new Date();
        currencyService.deleteCurrency(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Currency", HttpStatus.OK.value(),"Success", currency_name + " has been deleted.",null), HttpStatus.OK);
        //return new ResponseEntity<>(new ApiResponse(true, currency_name + " has been deleted."), HttpStatus.OK);
    }
}
