/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.xplaza.backend.common.util.ApiResponse;

@ControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse> conflict(DataIntegrityViolationException ex) {
    String message = getMostSpecificMessage(ex);
    return new ResponseEntity<ApiResponse>(new ApiResponse(0, "", HttpStatus.CONFLICT.value(), "Failed", message, null),
        HttpStatus.CONFLICT);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse> accessDenied(AccessDeniedException ex) {
    String message = ex.getMessage();
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(0, "", HttpStatus.FORBIDDEN.value(), "Failed", message, null), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ApiResponse> validationException(ValidationException ex) {
    String message = ex.getMessage();
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(0, "", HttpStatus.UNPROCESSABLE_ENTITY.value(), "Failed", message, null),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponse> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    ex.printStackTrace();
    String message = ex.getMessage();
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(0, "", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed", message, null),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse> unhandledExceptions(Exception ex) {
    String message = NestedExceptionUtils.getMostSpecificCause(ex).getMessage();
    ex.printStackTrace();
    return new ResponseEntity<ApiResponse>(
        new ApiResponse(0, "", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed", message, null),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private String getMostSpecificMessage(DataIntegrityViolationException ex) {
    String message = NestedExceptionUtils.getMostSpecificCause(ex).getMessage();
    if (message.contains("Detail:")) {
      message = message.substring(message.indexOf("Detail:") + "Detail:".length());
    }
    return message;
  }
}
