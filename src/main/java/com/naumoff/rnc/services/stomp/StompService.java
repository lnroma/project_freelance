package com.naumoff.rnc.services.stomp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StompService {
    public String generateJson(Map<String, Object> json) {
        String payload = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            payload = mapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return payload;
    }
}
