package com.coding.coding.analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("log-analyzer")
public class Controller {

    @Autowired
    private Manager manager;

    @GetMapping(value = "/getLogDetails", produces = "application/json")
    public ResponseEntity<Object> getRelevantLogsDetails(@RequestParam String url) {
        return manager.getLogsDetailsFromGivenUrl(url);
    }
}
