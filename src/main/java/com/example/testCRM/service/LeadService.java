package com.example.testCRM.service;

import com.example.testCRM.model.Lead;
import com.example.testCRM.repository.LeadRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LeadService {
    private static final String WEB_HOOK = "https://caramelka.bitrix24.ru/rest/8g8xi2rz23ddi8g5/";

    private final ObjectMapper objectMapper;
    private final LeadRepository repository;

    public void create(Lead lead) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        repository.save(lead);
        String json = createJsonPayload(lead);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, createHeaders());

        restTemplate.postForEntity(WEB_HOOK + "crm.lead.add.json", requestEntity, String.class);
    }

    public String getLeadById(Long id) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = WEB_HOOK + "crm.lead.get.json?ID=" + id;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public String getAllLeads() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(createHeaders());

        ResponseEntity<String> response = restTemplate.postForEntity(
                WEB_HOOK + "crm.lead.list.json", requestEntity, String.class);
        return response.getBody();
    }

    public void deleteLeadById(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = WEB_HOOK + "crm.lead.delete.json?id=" + id;
        restTemplate.delete(url);
    }

    private String createJsonPayload(Lead lead) throws JsonProcessingException {
        Map<String, Object> map = Map.of(
                "FIELDS", Map.of(
                        "TITLE", lead.getTitle(),
                        "ASSIGNED_BY_ID", 0
                )
        );
        return objectMapper.writeValueAsString(map);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

