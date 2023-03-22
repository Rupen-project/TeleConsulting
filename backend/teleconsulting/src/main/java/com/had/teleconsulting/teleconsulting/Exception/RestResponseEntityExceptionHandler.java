package com.had.teleconsulting.teleconsulting.Exception;

import com.had.teleconsulting.teleconsulting.Bean.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice // helps to divert the all the controller here when any error occur
@ResponseStatus
public class RestResponseEntityExceptionHandler  extends ResponseEntityExceptionHandler {


    // whenever DoctorNotFoundException error throw this methods is
    // helps to give back the error message as per our ErrorMessage Entity
    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ErrorMessage> doctorNotFoundException(DoctorNotFoundException exception, WebRequest request){
        // creating of error message with help of error class
        ErrorMessage message=new ErrorMessage(HttpStatus.NOT_FOUND,exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorMessage> patientNotFoundException(PatientNotFoundException exception, WebRequest request){
        // creating of error message with help of error class
        ErrorMessage message=new ErrorMessage(HttpStatus.NOT_FOUND,exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> patientNotFoundException(Exception exception, WebRequest request){
        // creating of error message with help of error class
        ErrorMessage message=new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,exception.getMessage());

        return new ResponseEntity<>(message, HttpStatus.valueOf(500));
    }

    @ExceptionHandler(ResouseNotFoundException.class)
    public ResponseEntity<ErrorMessage> patientNotFoundException(ResouseNotFoundException exception, WebRequest request){
        // creating of error message with help of error class
        ErrorMessage message=new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,exception.getMessage());

        return new ResponseEntity<>(message, HttpStatus.valueOf(500));
    }
}
