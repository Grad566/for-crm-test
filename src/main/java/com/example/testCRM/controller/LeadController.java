package com.example.testCRM.controller;

import com.example.testCRM.model.Lead;
import com.example.testCRM.service.LeadService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/leads")
public class LeadController {
    private final LeadService service;

    @SneakyThrows
    @GetMapping(path = "/{id}")
    public String get(@PathVariable Long id) {
        return service.getLeadById(id);
    }

    @SneakyThrows
    @GetMapping
    public String getAll() {
        return service.getAllLeads();
    }

    @SneakyThrows
    @PostMapping()
    public Lead create(@RequestBody Lead lead) {
        service.create(lead);
        return lead;
    }

    @DeleteMapping
    public void delete(@PathVariable Long id) {
        service.deleteLeadById(id);
    }
}
