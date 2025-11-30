package com.obelix.pi.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * Global exception handler for the project (package com.obelix.pi).
 *
 * Behavior:
 *  - Trata exceptions comuns do Spring (JSON inválido, validação, violação de integridade, etc).
 *  - Mappea mensagens em português lançadas como RuntimeException para status apropriados:
 *      * se a mensagem contém "não encontrado" -> 404 NOT FOUND
 *      * se a mensagem contém termos de validação/entrada -> 400 BAD REQUEST
 *      * fallback -> 500 INTERNAL SERVER ERROR com mensagem genérica
 *
 * Ajuste os critérios de mapeamento conforme necessário.
 */
@RestControllerAdvice(basePackages = "com.obelix.pi")
public class GlobalExceptionHandler {

    private String getPath(HttpServletRequest req) {
        return req == null ? "" : req.getRequestURI();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ApiSubError> subErrors = new ArrayList<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            subErrors.add(new ValidationSubError(fe.getObjectName(), fe.getField(), fe.getRejectedValue(), fe.getDefaultMessage()));
        }
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Erros de validação nos campos da requisição.",
                getPath(req));
        error.setErrors(subErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        List<ApiSubError> subErrors = new ArrayList<>();
        for (ConstraintViolation<?> v : violations) {
            subErrors.add(new ValidationSubError(v.getRootBeanClass().getSimpleName(),
                    v.getPropertyPath().toString(),
                    v.getInvalidValue(),
                    v.getMessage()));
        }
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                "Constraint Violation",
                "Erros de validação de parâmetros.",
                getPath(req));
        error.setErrors(subErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON request",
                ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage(),
                getPath(req));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(),
                "Data Integrity Violation",
                ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage(),
                getPath(req));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                ex.getMessage(),
                getPath(req));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        ErrorResponse error = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Not Allowed",
                ex.getMessage(),
                getPath(req));
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchElement(NoSuchElementException ex, HttpServletRequest req) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                getPath(req));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest req) {
        String msg = ex.getMessage() == null ? "" : ex.getMessage().toLowerCase();

        // mapear mensagens em português que tipicamente indicam "not found"
        if (msg.contains("não encontrado") || msg.contains("não encontrada") || msg.contains("nao encontrado") || msg.contains("nao encontrada") || msg.contains("não existe") || msg.contains("nao existe")) {
            ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    ex.getMessage(),
                    getPath(req));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // termos que usualmente indicam request inválida / validação
        if (msg.contains("por favor") || msg.contains("preencha") || msg.contains("senha incorreta") || msg.contains("senha deve") || msg.contains("obrigat") || msg.contains("inval") || msg.contains("já existe") || msg.contains("ja existe") || msg.contains("disponíve") || msg.contains("disponivel")) {
            ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    ex.getMessage(),
                    getPath(req));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // fallback: não expor detalhes internos ao cliente
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro inesperado. Entre em contato com o administrador.",
                getPath(req));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // helper inner class to represent field-level validation errors
    private static class ValidationSubError extends ApiSubError {
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;

        public ValidationSubError(String object, String field, Object rejectedValue, String message) {
            this.object = object;
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        public String getObject() { return object; }
        public String getField() { return field; }
        public Object getRejectedValue() { return rejectedValue; }
        public String getMessage() { return message; }
    }
}