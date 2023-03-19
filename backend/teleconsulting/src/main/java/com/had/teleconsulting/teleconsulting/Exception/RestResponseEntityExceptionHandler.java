package com.had.teleconsulting.teleconsulting.Exception;

import com.had.teleconsulting.teleconsulting.Bean.ErrorMessage;
import net.bytebuddy.asm.Advice;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.sql.rowset.WebRowSet;

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

    @ExceptionHandler(PatientNotFoundExeption.class)
    public ResponseEntity<ErrorMessage> patientNotFoundException(PatientNotFoundExeption exception, WebRequest request){
        // creating of error message with help of error class
        ErrorMessage message=new ErrorMessage(HttpStatus.NOT_FOUND,exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
