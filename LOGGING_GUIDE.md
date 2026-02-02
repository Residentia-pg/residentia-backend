# Residentia Logging Configuration Guide

## Overview
This guide explains the comprehensive logging system configured for the Residentia application using Spring Boot, SLF4J, and Logback.

## Architecture

### Components
1. **Logback Configuration** (`logback-spring.xml`) - Main logging configuration
2. **ActionLogger** - Utility class for structured user action logging
3. **LoggingAspect** - AOP aspect for automatic method logging
4. **Controller Logging** - Integrated logging in controllers

## Log Files Structure

### Location
All logs are written to the `./logs` directory:

```
logs/
├── residentia-app.log                 # All logs (INFO, DEBUG, ERROR)
├── residentia-app-error.log           # ERROR logs only
├── residentia-app-client-actions.log  # Client-specific actions
├── residentia-app-owner-actions.log   # Owner-specific actions
├── residentia-app-admin-actions.log   # Admin-specific actions
└── archived/                          # Old logs (compressed)
    ├── residentia-app.2026-02-01.0.log.gz
    └── residentia-app-error.2026-02-01.0.log.gz
```

### Log Rotation
- **Size**: Files rotate when they reach 10MB
- **Time**: Daily rotation at midnight
- **Retention**: 30 days for regular logs, 60 days for error logs
- **Total Size**: Maximum 1GB total log storage
- **Compression**: Old logs are gzip compressed

## Log Format

### Standard Log Format
```
YYYY-MM-DD HH:mm:ss.SSS [thread] LEVEL logger - message
```

Example:
```
2026-02-02 10:30:45.123 [http-nio-8888-exec-1] INFO  com.residentia.controller.ClientBookingController - Creating booking for user: John Doe
```

### Action Log Format (Client/Owner/Admin)
```
YYYY-MM-DD HH:mm:ss.SSS | USER: userId | EMAIL: userEmail | ACTION: action details
```

Example:
```
2026-02-02 10:30:45.123 | USER: 123 | EMAIL: john.doe@example.com | ACTION: CREATE_BOOKING | Details: PropertyID: 456, Duration: 6 months
```

## Logging Levels

### Level Hierarchy (from most to least verbose)
1. **DEBUG** - Detailed information for diagnosing problems
2. **INFO** - Informational messages highlighting application progress
3. **WARN** - Potentially harmful situations
4. **ERROR** - Error events that might still allow the application to continue

### Current Configuration
```yaml
com.residentia: DEBUG            # Your application code
org.springframework.web: INFO    # Spring Web logs
org.springframework.security: INFO  # Security logs
org.hibernate: INFO              # Database logs
```

## Usage Examples

### 1. INFO Level - Business Events

```java
@Slf4j
@RestController
public class MyController {
    
    @Autowired
    private ActionLogger actionLogger;
    
    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO dto) {
        // Simple INFO log
        log.info("Processing booking request for property: {}", dto.getPropertyId());
        
        // Structured action log
        actionLogger.logClientAction(
            userId,
            userEmail,
            "CREATE_BOOKING",
            String.format("PropertyID: %d, CheckIn: %s", propertyId, checkInDate)
        );
        
        return ResponseEntity.ok(booking);
    }
}
```

**Output in `residentia-app-client-actions.log`:**
```
2026-02-02 10:30:45.123 | USER: 123 | EMAIL: john@example.com | ACTION: CREATE_BOOKING | Details: PropertyID: 456, CheckIn: 2026-03-01
```

### 2. DEBUG Level - Troubleshooting

```java
@Slf4j
@Service
public class BookingService {
    
    public Booking processBooking(BookingDTO dto) {
        log.debug("Validating booking data: {}", dto);
        
        log.debug("Checking property availability for propertyId: {}", dto.getPropertyId());
        
        if (log.isDebugEnabled()) {
            log.debug("Full property details: {}", expensiveToStringOperation());
        }
        
        return booking;
    }
}
```

**Output:**
```
2026-02-02 10:30:45.123 [http-nio-8888-exec-1] DEBUG com.residentia.service.BookingService - Validating booking data: BookingDTO(propertyId=456...)
2026-02-02 10:30:45.124 [http-nio-8888-exec-1] DEBUG com.residentia.service.BookingService - Checking property availability for propertyId: 456
```

### 3. ERROR Level - Exception Handling

```java
@Slf4j
@RestController
public class PropertyController {
    
    @Autowired
    private ActionLogger actionLogger;
    
    @PostMapping("/properties")
    public ResponseEntity<?> addProperty(@RequestBody PropertyDTO dto) {
        try {
            Property property = propertyService.save(dto);
            return ResponseEntity.ok(property);
            
        } catch (ValidationException e) {
            log.error("Property validation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to add property | Owner: {} | Error: {}", 
                ownerEmail, e.getMessage(), e);
                
            actionLogger.logError("OWNER", ownerEmail, "ADD_PROPERTY", e);
            
            return ResponseEntity.status(500).body("Internal error");
        }
    }
}
```

**Output in `residentia-app-error.log`:**
```
2026-02-02 10:30:45.123 [http-nio-8888-exec-1] ERROR com.residentia.controller.PropertyController - Failed to add property | Owner: owner@example.com | Error: Database connection timeout
java.sql.SQLException: Connection timeout
    at com.mysql.cj.jdbc.ConnectionImpl.connectWithRetries(...)
    ...
```

## ActionLogger API Reference

### Client Actions
```java
// Full context
actionLogger.logClientAction(userId, email, action, details);

// Minimal context
actionLogger.logClientAction(email, action);
```

### Owner Actions
```java
// Full context
actionLogger.logOwnerAction(ownerId, email, action, details);

// Minimal context
actionLogger.logOwnerAction(email, action);
```

### Admin Actions
```java
// Full context
actionLogger.logAdminAction(adminId, email, action, targetEntity, targetId);

// Simplified
actionLogger.logAdminAction(email, action, details);
```

### Error Logging
```java
actionLogger.logError(userType, email, action, exception);
```

## Automatic Logging with LoggingAspect

The `LoggingAspect` automatically logs all controller method invocations:

```java
// No explicit logging needed in controller
@PostMapping("/bookings")
public ResponseEntity<?> createBooking(@RequestBody BookingDTO dto) {
    // Automatically logged by aspect:
    // - Request method and URI
    // - User email from JWT
    // - Execution time
    // - Success/failure status
    
    Booking booking = bookingService.createBooking(dto);
    return ResponseEntity.ok(booking);
}
```

**Automatic output:**
```
2026-02-02 10:30:45.123 [http-nio-8888-exec-1] DEBUG com.residentia.aspect.LoggingAspect - 📍 CLIENT Request: POST /api/client/properties/456/bookings | Method: ClientBookingController.createBooking | User: john@example.com
2026-02-02 10:30:45.567 [http-nio-8888-exec-1] INFO  com.residentia.aspect.LoggingAspect - ✅ CLIENT Action Completed: ClientBookingController.createBooking | User: john@example.com | Duration: 444ms
```

## Best Practices

### ✅ DO

1. **Use parameterized logging**
   ```java
   log.info("User {} created booking {}", email, bookingId);
   ```

2. **Log at appropriate levels**
   - DEBUG: Method entry/exit, variable values
   - INFO: Business events (login, booking, payment)
   - WARN: Recoverable issues (retry attempts)
   - ERROR: Exceptions and failures

3. **Include context in logs**
   ```java
   log.error("Payment failed | User: {} | Amount: {} | Error: {}", 
       email, amount, e.getMessage(), e);
   ```

4. **Use ActionLogger for user actions**
   ```java
   actionLogger.logClientAction(userId, email, "CREATE_BOOKING", "PropertyID: 123");
   ```

5. **Check log level for expensive operations**
   ```java
   if (log.isDebugEnabled()) {
       log.debug("Expensive data: {}", computeExpensiveData());
   }
   ```

### ❌ DON'T

1. **Don't use string concatenation**
   ```java
   // BAD
   log.info("User " + email + " created booking " + bookingId);
   ```

2. **Don't log sensitive data**
   ```java
   // BAD - passwords, credit cards, etc.
   log.debug("User password: {}", password);
   ```

3. **Don't log in tight loops without checks**
   ```java
   // BAD
   for (Item item : millionsOfItems) {
       log.debug("Processing: {}", item);  // Too much logging
   }
   ```

4. **Don't swallow exceptions silently**
   ```java
   // BAD
   catch (Exception e) {
       // Silent failure
   }
   
   // GOOD
   catch (Exception e) {
       log.error("Failed to process", e);
   }
   ```

## Monitoring Log Files

### View real-time logs
```bash
# All logs
tail -f logs/residentia-app.log

# Client actions only
tail -f logs/residentia-app-client-actions.log

# Errors only
tail -f logs/residentia-app-error.log
```

### Search logs
```bash
# Find all bookings by a user
grep "john@example.com" logs/residentia-app-client-actions.log

# Find all errors in last 24 hours
find logs/ -name "*.log" -mtime -1 -exec grep "ERROR" {} \;

# Count errors by type
grep "ERROR" logs/residentia-app-error.log | cut -d'|' -f1 | sort | uniq -c
```

## Configuration Changes

### Enable DEBUG for specific package
Edit `application.yml`:
```yaml
logging:
  level:
    com.residentia.service: DEBUG
    com.residentia.controller.BookingController: TRACE
```

### Change log file location
Edit `logback-spring.xml`:
```xml
<property name="LOG_PATH" value="/var/log/residentia"/>
```

### Adjust rotation settings
```xml
<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
    <fileNamePattern>${LOG_PATH}/archived/${LOG_FILE}.%d{yyyy-MM-dd}.log.gz</fileNamePattern>
    <maxHistory>90</maxHistory>  <!-- Keep 90 days -->
    <totalSizeCap>5GB</totalSizeCap>  <!-- Max 5GB total -->
</rollingPolicy>
```

## Troubleshooting

### Logs not appearing
1. Check file permissions on `logs/` directory
2. Verify `logback-spring.xml` is in `src/main/resources/`
3. Check console for Logback initialization errors
4. Verify log level is not set too high (e.g., ERROR only)

### Too many logs
1. Increase log level in `application.yml`:
   ```yaml
   logging:
     level:
       com.residentia: INFO  # Change from DEBUG
   ```

2. Adjust rolling policy to rotate more frequently

### Performance impact
1. Use async appenders (already configured)
2. Check if expensive operations are being logged at DEBUG level
3. Use conditional logging for expensive operations

## Summary

This logging configuration provides:
- ✅ Separate log files for different user types
- ✅ Automatic rotation and compression
- ✅ Structured logging with user context
- ✅ Automatic method invocation logging via AOP
- ✅ Different log levels for different purposes
- ✅ Performance-optimized with async appenders
- ✅ Easy to search and analyze logs

All logs are written to text files with structured information about who did what and when, making it easy to track user actions and debug issues.
