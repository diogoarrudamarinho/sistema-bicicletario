package dev.unirio.equipmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    
    private boolean status;
    private String message;
    private T data;
    
}
