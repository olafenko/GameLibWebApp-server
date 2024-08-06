package org.backend.gamelibwebapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class CustomExceptionHandler {

        public ResponseEntity<String> handleResourceAlreadyExistsException() {
            
        }



}
