package com.example.Import_Export_Data.service;

import com.example.Import_Export_Data.DTO.SourceDbConfig;
import org.springframework.stereotype.Service;

@Service
public class TemporarySourceDatabaseStore {
    private SourceDbConfig config;

    public void save(SourceDbConfig config) {
        this.config = config;
    }

    public SourceDbConfig get() {
        return config;
    }

    public boolean isAvailable() {
        return config != null;
    }

    public void clear() {
        config = null;
    }
} 