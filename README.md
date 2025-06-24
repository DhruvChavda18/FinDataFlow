## Project Overview
The Import Export Data application is a Spring Boot-based system designed to facilitate the transfer of accounting data between different PostgreSQL databases. It provides a robust solution for importing and exporting chart of accounts and related financial data structures between source and destination databases.

## Technology Stack
- Framework : Spring Boot 3.4.4
- Language : Java 21
- Database : PostgreSQL
- ORM : Hibernate/JPA
- Build Tool : Maven
- Frontend Templating : Thymeleaf
- Logging : Logback with SLF4J
  
## Key Features
- Dynamic Database Connections : Connect to different source and destination databases at runtime
- Data Transfer : Transfer chart of accounts and related financial data between databases
- Database Configuration Management : Configure and validate database connections through a user interface
- Soft Delete Support : Implement soft delete functionality for data versioning
- Comprehensive Logging : Detailed logging with trace IDs for debugging and auditing
- Error Handling : Global exception handling with appropriate HTTP responses
  
## Project Structure
### Main Components Controllers
- DataTransferController : Handles data transfer operations between databases
- DatabaseConfigController : Manages database configuration settings
- IndexController : Serves the main application pages
- LedgerController : Manages ledger-related operations
- LoginController : Handles user authentication Services
- DynamicDataTransferService : Core service for transferring data between databases
- DatabaseCreationService : Creates new databases when needed
- SourceDatabaseValidationService : Validates source database connections
- TemporaryDatabaseStore : In-memory storage for database configurations Data Models Source Entities
- MasterChartOfAccount : Chart of accounts from source database
- AccountProductionMasterSection : Account sections from source
- AccountProductionSubSections : Account subsections from source
- AccountProductionMasterTable : Account tables from source Destination Entities
- DestinationMasterChartOfAccount : Chart of accounts for destination
- DestinationAccountProductionMasterSection : Account sections for destination
- DestinationAccountProductionSubSections : Account subsections for destination
- DestinationAccountProductionMasterTable : Account tables for destination DTOs
- SourceDbConfig : Configuration for source database connections
- DestinationDbConfig : Configuration for destination database connections
- FullLedgerInfoDTO : Data transfer object for ledger information Configuration
- DynamicDatabaseConfig : Configures dynamic database connections
- AopConfig : Aspect-Oriented Programming configuration
- LoggingAspect : Logging aspects for method execution
- TraceIdFilter : HTTP filter for adding trace IDs to requests
  
## Setup and Configuration
### Prerequisites
- Java 21 or higher
- PostgreSQL database server
- Maven
  
### Database Configuration
The application requires a ledger database for its own operation, configured in application.properties :

```
spring.datasource.ledger.
jdbc-url=jdbc:postgresql://localhost:5432/
ledger
spring.datasource.ledger.username=yourusername
spring.datasource.ledger.password=yourpassword
spring.datasource.ledger.
driver-class-name=org.postgresql.Driver
```

### Running the Application
1. Clone the repository
2. Configure the database settings in application.properties
3. Run the application using Maven:
   ```
   ./mvnw spring-boot:run
   ```
4. Access the application at http://localhost:9090
   
## Usage
### Configuring Source Database
1. Navigate to the database configuration page
2. Enter source database connection details
3. Submit the form to validate and save the configuration
### Configuring Destination Database
1. Navigate to the database configuration page
2. Enter destination database connection details
3. Submit the form to create/connect to the destination database
### Transferring Data
1. Ensure both source and destination databases are configured
2. Select the chart of account to transfer
3. Initiate the transfer process
4. Monitor the transfer status through the UI or logs
   
## Logging
The application uses a comprehensive logging system with the following features:

- Separate log files for general logs and errors
- Trace IDs for request tracking
- Detailed logging for debugging
- Log rotation and archiving
  
## Error Handling
The application includes a global exception handler that provides appropriate HTTP responses for different types of errors, with detailed error messages logged for troubleshooting.

## Security Considerations
- Database credentials are stored in memory during runtime
- Proper input validation is implemented for database configuration
- Error messages are sanitized to prevent information disclosure
  
## Future Enhancements
- Add user authentication and authorization
- Implement scheduled data transfers
- Add support for additional database types
- Enhance the user interface with progress indicators
- Add data validation and transformation capabilities
