package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.State;
import com.backend.xplaza.service.StateService;
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
@RequestMapping("/api/state")
public class StateController {
    @Autowired
    private StateService stateService;
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
    public ResponseEntity<String> getStates() throws JsonProcessingException {
        start = new Date();
        List<State> dtos = stateService.listStates();
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Category List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getState (@PathVariable @Valid long id) throws JsonProcessingException {
        start = new Date();
        State dtos = stateService.listState(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"City List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value= "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addState (@RequestBody @Valid State state) {
        start = new Date();
        stateService.addState(state);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add State", HttpStatus.CREATED.value(),"Success", "State has been created.",null), HttpStatus.CREATED);
    }

    @PutMapping(value= "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateState (@RequestBody @Valid State state) {
        start = new Date();
        stateService.updateState(state);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update State", HttpStatus.OK.value(),"Success", "State has been updated.",null), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteState (@PathVariable @Valid long id) {
        String state_name = stateService.getStateNameByID(id);
        start = new Date();
        stateService.deleteState(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete State", HttpStatus.OK.value(),"Success", state_name + " has been deleted.",null), HttpStatus.OK);
    }
}
