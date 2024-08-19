package org.backend.gamelibwebapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

        @ExceptionHandler(ResourceAlreadyExistsException.class)
        public ResponseEntity<String> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
        }
        @ExceptionHandler(CannotPerformActionException.class)
        public ResponseEntity<String> handleCannotPerformActionException(CannotPerformActionException ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }


}
