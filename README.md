# FinDataFlow - Import Export Data Application

A Spring Boot-based system designed to facilitate the transfer of accounting data between different PostgreSQL databases. This application provides a robust solution for importing and exporting chart of accounts and related financial data structures between source and destination databases.

## 🚀 Features

- **Dynamic Database Connections**: Connect to different source and destination databases at runtime
- **Data Transfer**: Transfer chart of accounts and related financial data between databases
- **Database Configuration Management**: Configure and validate database connections through a user interface
- **Soft Delete Support**: Implement soft delete functionality for data versioning
- **Comprehensive Logging**: Detailed logging with trace IDs for debugging and auditing
- **Error Handling**: Global exception handling with appropriate HTTP responses
- **Web Interface**: User-friendly web interface built with Thymeleaf and Bootstrap

## 🛠 Technology Stack

- **Framework**: Spring Boot 3.4.4
- **Language**: Java 21
- **Database**: PostgreSQL
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven
- **Frontend Templating**: Thymeleaf
- **Logging**: Logback with SLF4J
- **UI Framework**: Bootstrap 5

## 📋 Prerequisites

- Java 21 or higher
- PostgreSQL database server
- Maven 3.6+
- Git

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd FinDataFlow
```

### 2. Database Setup
Create a PostgreSQL database for the application:
```sql
CREATE DATABASE ledger;
```

### 3. Configuration
Update the database configuration in `src/main/resources/application.properties`:

```properties
# Ledger Database Configuration
spring.datasource.ledger.jdbc-url=jdbc:postgresql://localhost:5432/ledger
spring.datasource.ledger.username=your_username
spring.datasource.ledger.password=your_password
spring.datasource.ledger.driver-class-name=org.postgresql.Driver

# Application Configuration
server.port=9090
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Build and Run
```bash
# Using Maven wrapper
./mvnw clean install
./mvnw spring-boot:run

# Or using Maven directly
mvn clean install
mvn spring-boot:run
```

### 5. Access the Application
Open your browser and navigate to: http://localhost:9090

## 📁 Project Structure

```
FinDataFlow/
├── src/main/java/com/example/Import_Export_Data/
│   ├── config/                 # Configuration classes
│   ├── controller/             # REST controllers
│   ├── DTO/                    # Data Transfer Objects
│   ├── entity/                 # JPA entities
│   │   ├── source/            # Source database entities
│   │   ├── destination/       # Destination database entities
│   │   └── ledgerEntity/      # Ledger entities
│   ├── repository/            # Data access layer
│   ├── service/               # Business logic services
│   └── ImportExportDataApplication.java
├── src/main/resources/
│   ├── static/                # Static assets (CSS, JS, images)
│   ├── templates/             # Thymeleaf templates
│   ├── application.properties # Application configuration
│   └── logback-spring.xml     # Logging configuration
└── logs/                      # Application logs
```

## 🔧 Configuration

### Database Configuration
The application requires a ledger database for its own operation. Configure the following in `application.properties`:

```properties
# Ledger Database
spring.datasource.ledger.jdbc-url=jdbc:postgresql://localhost:5432/ledger
spring.datasource.ledger.username=your_username
spring.datasource.ledger.password=your_password
spring.datasource.ledger.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server Configuration
server.port=9090
```

### Logging Configuration
The application uses Logback for logging. Configuration is in `logback-spring.xml`:
- Separate log files for general logs and errors
- Trace IDs for request tracking
- Log rotation and archiving

## 📑 Required Source Database Schema

Your source database **must** contain the following tables (in the `master` schema) with the exact column names as listed below.  
**All tables must exist and contain data.**  
If any table is missing, has different column names, or is empty, the data transfer will fail.

---

### 1. `master_chart_of_account`
- `id` (PK)
- `chartofaccountname`
- `description`
- `p_version`
- `prev_chart_of_account_id`
- `industry_id`
- `is_deleted`
- `is_default`
- `created_date`
- `created_by`
- `updated_date`
- `updated_by`
- `is_active`
- `status_id`
- `is_versioning`
- `is_copy_chartofaccount`
- `country_id`
- `is_substantial_change`

---

### 2. `account_production_master_sections`
- `id` (PK)
- `menu_name`
- `parentid`
- `serialnumber`
- `menutype`
- `is_editable`
- `is_active`
- `is_deleted`
- `created_by`
- `created_date`
- `updated_by`
- `updated_date`
- `ap_version`
- `alias_name`
- `original_section_id`

---

### 3. `account_production_sub_sections`
- `id` (PK)
- `menu_name`
- `master_section_id`
- `master_chart_of_account_id`
- `serialnumber`
- `menutype`
- `is_editable`
- `is_active`
- `is_deleted`
- `created_by`
- `created_date`
- `updated_by`
- `updated_date`
- `ap_version`
- `alias_name`
- `is_loader`
- `section_sequence`
- `cash_flow_type`
- `original_sub_section_id`

---

### 4. `account_production_master_tables`
- `id` (PK)
- `menu_name`
- `master_section_id`
- `sub_section_id`
- `master_chart_of_account_id`
- `table_json`
- `reference_id`
- `is_editable`
- `is_active`
- `is_deleted`
- `created_by`
- `created_date`
- `updated_by`
- `updated_date`
- `ap_version`
- `header_type`
- `checklist_question_reference`
- `sequence_number`
- `fsa_area_id`
- `original_table_id`
- `isckeditorcheck`

---

## ⚠️ Troubleshooting Schema Mismatches

- If you receive an error about missing or mismatched tables/columns, ensure your source database matches the schema above.
- All required tables must contain data; empty tables will cause the transfer to fail.
- Table and column names are **case-sensitive** and must match exactly.

## 🔑 Default Login Credentials

The application uses static credentials for login (for demo/development purposes):

- **Username:** `user`
- **Password:** `user`

> ⚠️ **Security Note:**
> These credentials are hardcoded for demonstration and development only.
> For production, implement a secure authentication mechanism and change/remove these defaults.

## 📖 Usage Guide

### 1. Configuring Source Database
1. Navigate to the database configuration page
2. Enter source database connection details:
   - Host and port
   - Database name
   - Username and password
3. Submit the form to validate and save the configuration

### 2. Configuring Destination Database
1. Navigate to the database configuration page
2. Enter destination database connection details
3. Submit the form to create/connect to the destination database

### 3. Transferring Data
1. Ensure both source and destination databases are configured
2. Select the chart of account to transfer
3. Initiate the transfer process
4. Monitor the transfer status through the UI or logs

## 🔍 API Endpoints

### Database Configuration
- `GET /config` - Database configuration page
- `POST /config/source` - Configure source database
- `POST /config/destination` - Configure destination database

### Data Transfer
- `GET /transfer` - Data transfer page
- `POST /transfer/initiate` - Initiate data transfer
- `GET /transfer/status` - Get transfer status

### Ledger Management
- `GET /ledger` - Ledger management page
- `POST /ledger/create` - Create new ledger entry
- `GET /ledger/list` - List all ledgers


## 📝 Logging

The application provides comprehensive logging:
- **Application Logs**: `logs/application.log`
- **Error Logs**: `logs/error.log`
- **Trace IDs**: Each request gets a unique trace ID for tracking

### Log Levels
- `DEBUG`: Detailed debugging information
- `INFO`: General application information
- `WARN`: Warning messages
- `ERROR`: Error messages

## 🔒 Security Considerations

- Database credentials are stored in memory during runtime
- Proper input validation is implemented for database configuration
- Error messages are sanitized to prevent information disclosure
- HTTPS is recommended for production deployments

## 🚨 Error Handling

The application includes a global exception handler that provides:
- Appropriate HTTP responses for different error types
- Detailed error messages logged for troubleshooting
- User-friendly error pages

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request


## 🔮 Future Enhancements

- [ ] Add user authentication and authorization
- [ ] Implement scheduled data transfers
- [ ] Add support for additional database types (MySQL, Oracle)
- [ ] Enhance the user interface with progress indicators
- [ ] Add data validation and transformation capabilities
- [ ] Implement data backup and restore functionality
- [ ] Add support for data encryption during transfer
- [ ] Create REST API for external integrations




---

**Note**: This application is designed for transferring accounting data between PostgreSQL databases. Ensure you have proper backups before performing any data transfer operations.
