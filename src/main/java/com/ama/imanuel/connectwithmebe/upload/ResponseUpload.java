package com.ama.imanuel.connectwithmebe.upload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUpload {
    @JsonProperty("object_name")
    String objectName;
    String url;
}
