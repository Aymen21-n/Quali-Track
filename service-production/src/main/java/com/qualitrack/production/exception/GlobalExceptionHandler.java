package com.qualitrack.production.exception;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ApiError> feignNotFound(FeignException.NotFound ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Ressource externe introuvable", request, List.of(), ex);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiError> feign(FeignException ex, HttpServletRequest request) {
        if (ex.status() == -1) {
            return response(HttpStatus.SERVICE_UNAVAILABLE, "Échec de communication avec un service", request, List.of(), ex);
        }
        if (ex.status() == 503) {
            return response(HttpStatus.SERVICE_UNAVAILABLE, "Service ou instance indisponible", request, List.of(), ex);
        }
        return response(HttpStatus.BAD_GATEWAY, "Erreur HTTP du service distant : [" + ex.status() + "]", request, List.of(), ex);
    }

    @ExceptionHandler(RessourceIntrouvableException.class)
    public ResponseEntity<ApiError> notFound(RessourceIntrouvableException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage(), request, List.of(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> illegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage(), request, List.of(), null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> constraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Validation failed", request, List.of(), null);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiError> missingHeader(MissingRequestHeaderException ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Validation failed", request, List.of(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError).toList();
        return response(HttpStatus.BAD_REQUEST, "Validation failed", request, details, null);
    }

    @ExceptionHandler(StatutTransitionException.class)
    public ResponseEntity<ApiError> invalidStatus(StatutTransitionException ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage(), request, List.of(), null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> unreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Malformed request body or invalid value", request, List.of(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> conflict(DataIntegrityViolationException ex, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "A resource with conflicting data already exists", request, List.of(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> internal(Exception ex, HttpServletRequest request) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request, List.of(), null);
    }

    private String formatFieldError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }

    private ResponseEntity<ApiError> response(HttpStatus status, String message, HttpServletRequest request, List<String> details, Exception ex) {
        if (ex != null) {
            System.out.println("[Feign] status=" + ex.getClass().getSimpleName() + " cause=" + ex.getCause());
        }
        return ResponseEntity.status(status).body(new ApiError(Instant.now(), status.value(),
                status.getReasonPhrase(), message, request.getRequestURI(), details));
    }
}