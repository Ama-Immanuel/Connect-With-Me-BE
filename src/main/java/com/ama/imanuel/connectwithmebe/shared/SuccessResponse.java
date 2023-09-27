package com.ama.imanuel.connectwithmebe.shared;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse<T> {
    private SuccessDTO<T> response;

    public SuccessResponse(String message){
        this.response = new SuccessDTO<>(message);
    }

    public SuccessResponse(T object){
        this.response = new SuccessDTO<>(object);
    }

    public SuccessResponse(T object, String message){
        this.response = new SuccessDTO<>(object, message);
    }

    public SuccessResponse(T object, Integer length, String message){
        this.response = new SuccessDTO<>(object, length, message);
    }

    public SuccessResponse(T object, Integer length){
        this.response= new SuccessDTO<>(object, length);
    }
}
