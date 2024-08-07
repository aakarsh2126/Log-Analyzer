package com.coding.coding.analyzer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service
public class Manager {

    @Autowired
    private RestHandler restHandler;

    public ResponseEntity<Object> getLogsDetailsFromGivenUrl(String url) {
        try {
            ResponseDto responseDto = getLogsDetails(url);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseDto getLogsDetails(String url) {
        ResponseDto responseDto = new ResponseDto();
        String content = hitAPI(url);
        Map<String, Integer> totalRequestsPerEndpoint = getCountOfDiffferentEndPoints(content);
        Map<String, Integer> totalRequestsPerHour = getCountOfEndPointsPerHour(content);
        Map<String, Integer> p95ResponseTime = getP95ResponseTimeOfEachEndPoint(content);
        responseDto.setTotalRequestsPerEndpoint(totalRequestsPerEndpoint);
        responseDto.setTotalRequestsPerHour(totalRequestsPerHour);
        responseDto.setP95ResponseTimePerEndpoint(p95ResponseTime);
        return responseDto;
    }

    private String hitAPI(String url) {
        String content = restHandler.makeHttpGetCall(url);
        return content;
    }

    private Map<String, Integer> getCountOfDiffferentEndPoints(String content) {
        Map<String,Integer> uniqueEndpointsMap = new HashMap<>();
        String[] logs = content.split("\n");
        for (String log : logs) {
            String[] endpointData = log.split(" ");
            if (endpointData.length == 7 && endpointData[2].startsWith("url=")) {
                String url = endpointData[2];
                url = url.substring(4);
                uniqueEndpointsMap.put(url, uniqueEndpointsMap.containsKey(url) ? uniqueEndpointsMap.get(url) + 1 : 1);
            }
        }
        return uniqueEndpointsMap;
    }

    private Map<String, Integer> getCountOfEndPointsPerHour(String content) {
        Map<String,Integer> countPerHour = new HashMap<>();
        String[] logs = content.split("\n");
        for (String log : logs) {
            String[] endpointData = log.split(" ");
            if (endpointData.length == 7 && endpointData[2].startsWith("url=")) {
                String date = endpointData[0];
                String hour = Util.getTimeFromString(date);
                countPerHour.put(hour, countPerHour.containsKey(hour) ? countPerHour.get(hour) + 1 : 1);
            }
        }
        return countPerHour;
    }

    private Map<String, Integer> getP95ResponseTimeOfEachEndPoint(String content) {
        Map<String, Double> p95PerEndpoint = new HashMap<>();
        String[] logs = content.split("\n");
        for (String log : logs) {
            String[] endpointData = log.split(" ");
            if (endpointData.length == 7 && endpointData[2].startsWith("url=")) {
                String url = endpointData[2];
                url = url.substring(4);
                String responseTime = endpointData[3];
                responseTime = responseTime.substring(5);
                Double d = Double.parseDouble(responseTime)/1000000;
                p95PerEndpoint.put(url, p95PerEndpoint.containsKey(url) ? p95PerEndpoint.get(url) + d : d);
            }
        }
        Map<String, Integer> p95Map = new HashMap<>();
        for (Map.Entry<String, Double> entry : p95PerEndpoint.entrySet()) {
            String url = entry.getKey();
            Double value = entry.getValue();
            Double p95 = value*0.95;
            p95Map.put(url, p95.intValue());
        }
        return p95Map;
    }
}
