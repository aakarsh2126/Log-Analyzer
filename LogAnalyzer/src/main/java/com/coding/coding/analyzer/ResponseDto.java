package com.coding.coding.analyzer;

import lombok.Data;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseDto {
    private Map<String, Integer> totalRequestsPerEndpoint;
    private Map<String, Integer> p95ResponseTimePerEndpoint;
    private Map<String, Integer> totalRequestsPerHour;
}
