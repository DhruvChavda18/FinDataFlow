package com.example.Import_Export_Data.service;

import com.example.Import_Export_Data.DTO.DestinationDbConfig;
import com.example.Import_Export_Data.DTO.SourceDbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SourceDatabaseValidationService {
    private static final Logger logger = LoggerFactory.getLogger(SourceDatabaseValidationService.class);

    private static final Map<String, List<String>> REQUIRED_TABLES_AND_COLUMNS = new LinkedHashMap<>();

    static {
        REQUIRED_TABLES_AND_COLUMNS.put("master_chart_of_account", List.of(
            "id",
            "chartofaccountname",
            "description",
            "p_version",
            "prev_chart_of_account_id",
            "industry_id",
            "is_deleted",
            "is_default",
            "created_date",
            "created_by",
            "updated_date",
            "updated_by",
            "is_active",
            "status_id",
            "is_versioning",
            "is_copy_chartofaccount",
            "country_id",
            "is_substantial_change"
        ));

        REQUIRED_TABLES_AND_COLUMNS.put("account_production_master_sections", List.of(
            "id",
            "menu_name",
            "parentid",
            "serialnumber",
            "menutype",
            "is_editable",
            "is_active",
            "is_deleted",
            "created_by",
            "created_date",
            "updated_by",
            "updated_date",
            "ap_version",
            "alias_name",
            "original_section_id"
        ));

        REQUIRED_TABLES_AND_COLUMNS.put("account_production_sub_sections", List.of(
            "id",
            "menu_name",
            "master_section_id",
            "master_chart_of_account_id",
            "serialnumber",
            "menutype",
            "is_editable",
            "is_active",
            "is_deleted",
            "created_by",
            "created_date",
            "updated_by",
            "updated_date",
            "ap_version",
            "alias_name",
            "is_loader",
            "section_sequence",
            "cash_flow_type",
            "original_sub_section_id"
        ));

        REQUIRED_TABLES_AND_COLUMNS.put("account_production_master_tables", List.of(
            "id",
            "menu_name",
            "master_section_id",
            "sub_section_id",
            "master_chart_of_account_id",
            "table_json",
            "reference_id",
            "is_editable",
            "is_active",
            "is_deleted",
            "created_by",
            "created_date",
            "updated_by",
            "updated_date",
            "ap_version",
            "header_type",
            "checklist_question_reference",
            "sequence_number",
            "fsa_area_id",
            "original_table_id",
            "isckeditorcheck"
        ));

        REQUIRED_TABLES_AND_COLUMNS.put("account_production_input_sheet", List.of(
            "id",
            "chart_of_account_id",
            "description",
            "display_name",
            "is_active",
            "is_deleted",
            "created_by",
            "created_date",
            "updated_by",
            "updated_date",
            "version",
            "macter_code_id",
            "parent_section_id"
        ));

        REQUIRED_TABLES_AND_COLUMNS.put("account_production_ledger", List.of(
            "id",
            "master_chart_of_account_id",
            "debit",
            "credit",
            "reference_id",
            "created_date",
            "is_active",
            "is_deleted",
            "updated_date",
            "version",
            "is_group",
            "is_ledger",
            "is_sub_group",
            "ledger_name",
            "created_by",
            "updated_by",
            "ledger_type_id",
            "parent_id",
            "serialnumber",
            "ap_version",
            "fsa_area_id",
            "row_alias",
            "is_optional",
            "original_ledger_id",
            "srno"
        ));

        REQUIRED_TABLES_AND_COLUMNS.put("admin_policy_master", List.of(
            "id",
            "chart_of_account_id",
            "policy_id",
            "child_policy_id",
            "sub_child_policy_id",
            "header_name",
            "description",
            "parent_id",
            "serial_no",
            "type",
            "ap_version",
            "is_active",
            "is_deleted",
            "created_by",
            "created_date",
            "updated_by",
            "updated_date",
            "display_sequence",
            "original_policy_id",
            "filepath",
            "document_id",
            "filename",
            "note_count",
            "filetype",
            "is_updated",
            "version"
        ));
    }

    public ValidationResult validateSourceDatabase(SourceDbConfig config) {
        logger.info("Starting source database validation for database: {}", config.getDbName());
        return validateDatabaseSchema(config.getHost(), config.getPort(), config.getDbName(), config.getUsername(), config.getPassword());
    }

    public ValidationResult validateDestinationDatabase(DestinationDbConfig config) {
        logger.info("Starting destination database validation for database: {}", config.getDbName());
        return validateDatabaseSchema(config.getHost(), config.getPort(), config.getDbName(), config.getUsername(), config.getPassword());
    }

    private ValidationResult validateDatabaseSchema(String host, String port, String dbName, String username, String password) {
        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);
        List<String> errors = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            logger.debug("Successfully connected to database: {}", dbName);
            DatabaseMetaData metaData = conn.getMetaData();

            for (Map.Entry<String, List<String>> entry : REQUIRED_TABLES_AND_COLUMNS.entrySet()) {
                String tableName = entry.getKey();
                List<String> requiredColumns = entry.getValue();

                // 1. Check if the table exists in "master" schema
                logger.debug("Checking for required table: {}", tableName);
                try (ResultSet tables = metaData.getTables(null, "master", tableName, new String[]{"TABLE"})) {
                    if (!tables.next()) {
                        logger.warn("Required table is missing: master.{}", tableName);
                        errors.add(String.format("Table 'master.%s' is missing.", tableName));
                        continue;
                    }
                }

                // 2. Fetch actual columns in the database table
                List<String> actualColumns = new ArrayList<>();
                try (ResultSet cols = metaData.getColumns(null, "master", tableName, null)) {
                    while (cols.next()) {
                        String actualColName = cols.getString("COLUMN_NAME");
                        if (actualColName != null) {
                            actualColumns.add(actualColName.toLowerCase());
                        }
                    }
                }

                // 3. Verify each required column exists
                for (String expectedCol : requiredColumns) {
                    boolean found = false;
                    for (String actualCol : actualColumns) {
                        if (actualCol.equals(expectedCol)) {
                            found = true;
                            break;
                        }
                        // Flexible check for camelCase to snake_case conversions
                        if (expectedCol.equals("chartofaccountname") && actualCol.equals("chart_of_account_name")) {
                            found = true;
                            break;
                        }
                        if (expectedCol.equals("isckeditorcheck") && actualCol.equals("is_ckeditor_check")) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        logger.warn("Missing column '{}' in table 'master.{}'", expectedCol, tableName);
                        errors.add(String.format("Column '%s' is missing in table 'master.%s'.", expectedCol, tableName));
                    }
                }
            }

            if (errors.isEmpty()) {
                logger.info("Database schema validation successful - all required tables and columns present");
                return new ValidationResult(true, "Database, tables, and columns validated successfully!");
            } else {
                String fullErrorMessage = "Schema validation failed:\n- " + String.join("\n- ", errors);
                logger.error("Database schema validation failed: {}", fullErrorMessage);
                return new ValidationResult(false, fullErrorMessage);
            }

        } catch (SQLException e) {
            String connErrorMessage = "Failed to connect to database: " + e.getMessage();
            logger.error(connErrorMessage, e);
            return new ValidationResult(false, connErrorMessage);
        }
    }

    public static class ValidationResult {
        private final boolean success;
        private final String message;

        public ValidationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
