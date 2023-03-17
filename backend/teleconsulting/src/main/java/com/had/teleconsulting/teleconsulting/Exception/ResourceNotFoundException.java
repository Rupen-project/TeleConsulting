package com.had.teleconsulting.teleconsulting.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    String resouceName;
    String filedName;
    long fieldValue;

    public ResourceNotFoundException(String resouceName, String filedName, long fieldValue) {
        super(String.format("%s not found %s : %l",resouceName,filedName,fieldValue));
        this.resouceName = resouceName;
        this.filedName = filedName;
        this.fieldValue = fieldValue;
    }
}
