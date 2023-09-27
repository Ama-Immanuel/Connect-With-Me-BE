package com.ama.imanuel.connectwithmebe.socket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class MessageSocket {
    @JsonProperty("user_id")
    private String UserID;
    private String message;
    @JsonProperty("group_id")
    private String GroupID;
}
