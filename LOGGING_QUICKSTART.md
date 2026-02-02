# Quick Start: Logging in Residentia

## 1. Add to Your Controller

```java
import com.residentia.logging.ActionLogger;
import lombok.extern.slf4j.Slf4j;

@Slf4j  // Add this annotation
@RestController
@RequestMapping("/api/example")
public class ExampleController {
    
    @Autowired
    private ActionLogger actionLogger;  // Inject ActionLogger
    
    @PostMapping("/action")
    public ResponseEntity<?> performAction(@RequestBody RequestDTO dto,
                                          HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        
        try {
            // Your business logic here
            Result result = service.doSomething(dto);
            
            // Log the action
            actionLogger.logClientAction(
                userId,
                email,
                "ACTION_NAME",
                "Details about what happened"
            );
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Action failed for user: {}", email, e);
            actionLogger.logError("CLIENT", email, "ACTION_NAME", e);
            return ResponseEntity.status(500).body("Error");
        }
    }
}
```

## 2. Log Levels Quick Reference

### INFO - Track what's happening
```java
log.info("User {} logged in successfully", email);
log.info("Booking created: ID={}, Property={}", bookingId, propertyId);
```

### DEBUG - For development & troubleshooting
```java
log.debug("Validating input: {}", inputData);
log.debug("Database query returned {} results", count);
```

### ERROR - When things go wrong
```java
log.error("Failed to process payment: {}", e.getMessage(), e);
actionLogger.logError("CLIENT", email, "PAYMENT", e);
```

## 3. Action Logger Cheat Sheet

### Client Actions
```java
// Full details
actionLogger.logClientAction(
    123,                          // userId
    "client@example.com",        // email
    "CREATE_BOOKING",            // action
    "PropertyID: 456, Price: 5000"  // details
);

// Quick log
actionLogger.logClientAction("client@example.com", "VIEW_PROPERTY");
```

### Owner Actions
```java
// Full details
actionLogger.logOwnerAction(
    789L,                         // ownerId
    "owner@example.com",         // email
    "ADD_PROPERTY",              // action
    "PropertyName: Sunshine PG, Rooms: 10"  // details
);

// Quick log
actionLogger.logOwnerAction("owner@example.com", "UPDATE_PROPERTY");
```

### Admin Actions
```java
// Full details
actionLogger.logAdminAction(
    1,                           // adminId
    "admin@example.com",        // email
    "APPROVE_PROPERTY",         // action
    "Property",                 // targetEntity
    "456"                       // targetId
);

// Quick log
actionLogger.logAdminAction("admin@example.com", "DEACTIVATE_USER", "UserID: 123");
```

## 4. Common Patterns

### Pattern 1: Controller Method with Logging
```java
@PostMapping("/bookings")
public ResponseEntity<?> createBooking(@RequestBody BookingDTO dto,
                                      HttpServletRequest request) {
    String email = (String) request.getAttribute("email");
    log.info("Processing booking request for property: {}", dto.getPropertyId());
    
    try {
        Booking booking = bookingService.createBooking(dto);
        
        actionLogger.logClientAction(
            dto.getUserId(),
            email,
            "CREATE_BOOKING",
            String.format("PropertyID: %d, CheckIn: %s, Amount: %.2f",
                dto.getPropertyId(), dto.getCheckInDate(), booking.getAmount())
        );
        
        log.info("Booking created successfully: {}", booking.getId());
        return ResponseEntity.ok(booking);
        
    } catch (Exception e) {
        log.error("Booking creation failed for user {}: {}", email, e.getMessage(), e);
        actionLogger.logError("CLIENT", email, "CREATE_BOOKING", e);
        return ResponseEntity.status(500).body("Booking failed");
    }
}
```

### Pattern 2: Service Method with Debug Logging
```java
@Slf4j
@Service
public class PropertyService {
    
    public Property validateProperty(Long propertyId) {
        log.debug("Validating property: {}", propertyId);
        
        Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() -> new NotFoundException("Property not found"));
        
        log.debug("Property found: name={}, owner={}", 
            property.getName(), property.getOwner().getEmail());
        
        return property;
    }
}
```

### Pattern 3: Admin Action with Detailed Logging
```java
@PutMapping("/{id}/approve")
public ResponseEntity<?> approveProperty(@PathVariable Long id,
                                        HttpServletRequest request) {
    String adminEmail = (String) request.getAttribute("email");
    
    try {
        Property property = propertyService.approve(id);
        
        log.info("Property approved: ID={}, Name={}, Owner={}", 
            id, property.getName(), property.getOwner().getEmail());
        
        actionLogger.logAdminAction(
            null,
            adminEmail,
            "APPROVE_PROPERTY",
            "Property",
            String.valueOf(id)
        );
        
        return ResponseEntity.ok(property);
        
    } catch (Exception e) {
        log.error("Property approval failed: propertyId={}, error={}", 
            id, e.getMessage(), e);
        return ResponseEntity.status(500).body("Approval failed");
    }
}
```

## 5. Where Logs Are Saved

```
logs/
├── residentia-app.log                  # Everything
├── residentia-app-error.log            # Only errors
├── residentia-app-client-actions.log   # Client actions
├── residentia-app-owner-actions.log    # Owner actions
└── residentia-app-admin-actions.log    # Admin actions
```

## 6. View Logs in Real-Time

```powershell
# View all logs
Get-Content logs\residentia-app.log -Wait -Tail 50

# View only client actions
Get-Content logs\residentia-app-client-actions.log -Wait -Tail 20

# View only errors
Get-Content logs\residentia-app-error.log -Wait -Tail 20
```

## 7. Common Actions to Log

### Client Actions
- `LOGIN` - User login
- `REGISTER` - User registration
- `CREATE_BOOKING` - New booking
- `CANCEL_BOOKING` - Booking cancellation
- `VIEW_PROPERTY` - Property details viewed
- `SEARCH_PROPERTIES` - Property search
- `UPDATE_PROFILE` - Profile update
- `SUBMIT_REVIEW` - Review submission

### Owner Actions
- `LOGIN` - Owner login
- `REGISTER` - Owner registration
- `ADD_PROPERTY` - New property added
- `UPDATE_PROPERTY` - Property updated
- `DELETE_PROPERTY` - Property deleted
- `CREATE_CHANGE_REQUEST` - Change request created
- `VIEW_BOOKINGS` - Bookings viewed
- `UPDATE_PROFILE` - Profile update

### Admin Actions
- `LOGIN` - Admin login
- `APPROVE_PROPERTY` - Property approved
- `REJECT_PROPERTY` - Property rejected
- `APPROVE_REQUEST` - Change request approved
- `REJECT_REQUEST` - Change request rejected
- `DEACTIVATE_USER` - User deactivated
- `ACTIVATE_USER` - User activated
- `DELETE_USER` - User deleted
- `DEACTIVATE_OWNER` - Owner deactivated
- `VIEW_DASHBOARD` - Dashboard viewed

## 8. Testing Your Logs

1. **Start the application**
2. **Make a request** (e.g., create a booking)
3. **Check the log files** in the `logs/` directory
4. **Look for your action** in the appropriate log file

Example output:
```
2026-02-02 14:35:22.456 | USER: 123 | EMAIL: john@example.com | ACTION: CREATE_BOOKING | Details: PropertyID: 456, CheckIn: 2026-03-01, Amount: 15000.00
```

## 9. Best Practices Checklist

- ✅ Add `@Slf4j` annotation to your controller/service
- ✅ Inject `ActionLogger` if you need structured logging
- ✅ Use INFO for business events (login, booking, etc.)
- ✅ Use DEBUG for detailed troubleshooting
- ✅ Use ERROR for exceptions
- ✅ Always log exceptions with the exception object: `log.error("message", e)`
- ✅ Use parameterized logging: `log.info("User {}", email)`
- ❌ Don't log sensitive data (passwords, credit cards)
- ❌ Don't use string concatenation: `"User " + email`

## 10. Need Help?

See the full guide: [LOGGING_GUIDE.md](LOGGING_GUIDE.md)

Example code: [LoggingExamples.java](src/main/java/com/residentia/examples/LoggingExamples.java)
