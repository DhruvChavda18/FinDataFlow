package com.example.Import_Export_Data.DTO;

public class DestinationDbConfig {

    private String dbName;
    private String username;
    private String password;

    public DestinationDbConfig() {
    }

    public DestinationDbConfig(String dbName, String username, String password) {
        this.dbName = dbName;
        this.username = username;
        this.password = password;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

