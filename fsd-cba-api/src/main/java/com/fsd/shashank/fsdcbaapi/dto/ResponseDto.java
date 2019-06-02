package com.fsd.shashank.fsdcbaapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto {
    @JsonProperty("result")
    private String result;

    @JsonProperty("data")
    private Object object;
}
