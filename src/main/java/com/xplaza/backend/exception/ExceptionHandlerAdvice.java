/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.exception;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.xplaza.backend.common.util.ApiResponse;

/**
 * Global exception handler for all controllers. Ensures consistent API error
 * responses.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
  private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

  /**
   * Handles database constraint violations.
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse> conflict(DataIntegrityViolationException ex) {
    String message = getMostSpecificMessage(ex);
    logger.warn("Data integrity violation: {}", message);
    return new ResponseEntity<>(new ApiResponse(0, "", HttpStatus.CONFLICT.value(), "Failed", message, null),
        HttpStatus.CONFLICT);
  }

  /**
   * Handles access denied exceptions.
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse> accessDenied(AccessDeniedException ex) {
    String message = ex.getMessage();
    logger.warn("Access denied: {}", message);
    return new ResponseEntity<>(
        new ApiResponse(0, "", HttpStatus.FORBIDDEN.value(), "Failed", message, null), HttpStatus.FORBIDDEN);
  }

  /**
   * Handles custom business exceptions (including validation and resource not
   * found).
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse> handleBusinessException(BusinessException ex) {
    String message = ex.getMessage();
    logger.info("Business exception: {}", message);
    HttpStatus status;
    if (ex instanceof ResourceNotFoundException) {
      status = HttpStatus.NOT_FOUND;
    } else if (ex.getClass().getSimpleName().equals("ValidationException")) {
      status = HttpStatus.UNPROCESSABLE_ENTITY;
    } else {
      status = HttpStatus.BAD_REQUEST;
    }
    return new ResponseEntity<>(
        new ApiResponse(0, "", status.value(), "Failed", message, null), status);
  }

  /**
   * Handles argument type mismatches (e.g., invalid path variable types).
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponse> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    logger.warn("Method argument type mismatch: {}", ex.getMessage());
    String message = ex.getMessage();
    return new ResponseEntity<>(
        new ApiResponse(0, "", HttpStatus.BAD_REQUEST.value(), "Failed", message, null),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles validation errors from @Valid annotated DTOs.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    String errors = ex.getBindingResult().getFieldErrors().stream()
        .map(FieldError::getDefaultMessage)
        .collect(Collectors.joining(", "));
    logger.info("Validation failed: {}", errors);
    return new ResponseEntity<>(
        new ApiResponse(0, "", HttpStatus.UNPROCESSABLE_ENTITY.value(), "Failed", errors, null),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  /**
   * Handles all other unhandled exceptions.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse> unhandledExceptions(Exception ex) {
    logger.error("Unhandled exception: {}", ex.getMessage());
    String message = NestedExceptionUtils.getMostSpecificCause(ex).getMessage();
    return new ResponseEntity<>(
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
