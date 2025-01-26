# Financial Transaction Management API

This Spring Boot application provides a REST API for managing financial transactions, allowing users to create transactions, retrieve statistics, and perform other operations.

## Prerequisites

- Java 17 or higher
- Gradle 7.x or higher
- Docker (optional, for containerized deployment)

## Building the Application

### Using Gradle

1. Clone the repository:
```bash
git clone <repository-url>
cd desafio
```

2. Build the application:
```bash
./gradlew clean build
```

This will compile the code, run tests, and create an executable JAR file in the `build/libs` directory.

### Using Docker

1. Build the Docker image:
```bash
docker-compose build
```

## Running the Application

### Local Development

1. Run the application using Gradle:
```bash
./gradlew bootRun
```

The application will start on port 8080.

### Using Docker

1. Start the containerized application:
```bash
docker-compose up
```

The application will be available at `http://localhost:8080`.

## API Documentation

### Endpoints

#### 1. Create Transaction
- **URL**: `/transacao`
- **Method**: POST
- **Request Body**:
```json
{
    "valor": 100.50,
    "dataHora": "2023-12-10T10:00:00-03:00"
}
```
- **Response Codes**:
  - 201: Transaction created successfully
  - 400: Invalid transaction data
  - 422: Transaction with future date or older than 60 seconds

#### 2. Delete All Transactions
- **URL**: `/transacao`
- **Method**: DELETE
- **Response Code**: 200 (All transactions deleted successfully)

#### 3. Get Statistics
- **URL**: `/estatistica`
- **Method**: GET
- **Response Body**:
```json
{
    "count": 10,
    "sum": 1000.00,
    "avg": 100.00,
    "max": 200.00,
    "min": 50.00
}
```
- **Response Code**: 200

## Testing

Run the test suite using Gradle:
```bash
./gradlew test
```

## Environment Configuration

The application can be configured through `application.properties` or environment variables. Key configurations:

- `server.port`: HTTP port (default: 8080)
- `logging.level.com.samuel.itau`: Log level (default: INFO)

## Architecture

The application follows a layered architecture:
- **Controller Layer**: Handles HTTP requests/responses
- **Service Layer**: Contains business logic
- **Model Layer**: Defines data structures

Concurrency is handled using thread-safe data structures (ConcurrentLinkedQueue).

## Performance Considerations

- Transaction statistics are calculated in real-time
- Old transactions (>60 seconds) are automatically excluded from statistics
- Thread-safe implementation ensures data consistency in concurrent scenarios

## Support

For issues or questions, please open a GitHub issue in the repository.