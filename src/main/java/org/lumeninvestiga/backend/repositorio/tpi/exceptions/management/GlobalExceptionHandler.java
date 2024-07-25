package org.lumeninvestiga.backend.repositorio.tpi.exceptions.management;

import jakarta.servlet.http.HttpServletRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.response.ErrorResponse;
import org.lumeninvestiga.backend.repositorio.tpi.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<ErrorResponse> notFoundResourceException(NotFoundResourceException e,
                                                         HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDocumentFormatException.class)
    public ResponseEntity<ErrorResponse> invalidDocumentFormatException(InvalidDocumentFormatException e,
                                                         HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidResourceException.class)
    public ResponseEntity<ErrorResponse> invalidResourceException(InvalidResourceException e,
                                                         HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotContentCommentException.class)
    public ResponseEntity<ErrorResponse> notContentCommentException(NotContentCommentException e,
                                                         HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(), 
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReferenceNotFoundException.class)
    public ResponseEntity<ErrorResponse> referenceNotFoundException(ReferenceNotFoundException e,
                                                         HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceCountException.class)
    public ResponseEntity<ErrorResponse> resourceCountException(ResourceCountException e,
                                                                    HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SavingErrorException.class)
    public ResponseEntity<ErrorResponse> savingErrorException(SavingErrorException e,
                                                                HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRegisterException.class)
    public ResponseEntity<ErrorResponse> invalidRegistrationException(InvalidRegisterException e,
                                                        HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}