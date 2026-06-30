package com.example.Import_Export_Data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ImportExportDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImportExportDataApplication.class, args);
	}

}


