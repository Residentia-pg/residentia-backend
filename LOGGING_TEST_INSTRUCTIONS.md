# Testing the Logging System

## Quick Test Using the Test Controller

I've created a special test controller to verify your logging setup. Here's how to test it:

### 1. Start Your Application

```powershell
cd server\residentia-backend
mvn spring-boot:run
```

### 2. Test Endpoints

#### Test Basic Logging
```powershell
# Simple GET request to test logging
curl http://localhost:8888/api/test/logging
```

#### Test Client Action Logging
```powershell
# POST request with JSON body
curl -X POST http://localhost:8888/api/test/logging/client `
  -H "Content-Type: application/json" `
  -d '{"userId": 123, "email": "client@test.com", "action": "TEST_BOOKING"}'
```

#### Test Owner Action Logging
```powershell
# POST request with JSON body
curl -X POST http://localhost:8888/api/test/logging/owner `
  -H "Content-Type: application/json" `
  -d '{"ownerId": 789, "email": "owner@test.com", "action": "TEST_PROPERTY"}'
```

#### Test Admin Action Logging
```powershell
# POST request with JSON body
curl -X POST http://localhost:8888/api/test/logging/admin `
  -H "Content-Type: application/json" `
  -d '{"adminId": 1, "email": "admin@test.com", "action": "TEST_APPROVAL"}'
```

#### Test Error Logging
```powershell
curl http://localhost:8888/api/test/logging/error
```

#### Test All Logging Levels
```powershell
curl http://localhost:8888/api/test/logging/all
```

#### Get Logging Status
```powershell
curl http://localhost:8888/api/test/logging/status
```

### 3. Check the Log Files

After making the requests above, check these files:

```powershell
# View all logs
Get-Content logs\residentia-app.log -Tail 20

# View client actions
Get-Content logs\residentia-app-client-actions.log -Tail 10

# View owner actions
Get-Content logs\residentia-app-owner-actions.log -Tail 10

# View admin actions
Get-Content logs\residentia-app-admin-actions.log -Tail 10

# View errors only
Get-Content logs\residentia-app-error.log -Tail 10
```

## What You Should See

### In Console (when running the app):
```
2026-02-02 14:30:45.123 [http-nio-8888-exec-1] INFO  c.r.controller.LoggingTestController - Testing CLIENT logging for user: client@test.com
2026-02-02 14:30:45.125 [http-nio-8888-exec-1] INFO  c.r.aspect.LoggingAspect - ✅ CLIENT Action Completed: LoggingTestController.testClientLogging | Duration: 5ms
```

### In `logs/residentia-app-client-actions.log`:
```
2026-02-02 14:30:45.124 | USER: 123 | EMAIL: client@test.com | ACTION: TEST_BOOKING | Details: Test action at 2026-02-02T14:30:45.124
```

### In `logs/residentia-app-owner-actions.log`:
```
2026-02-02 14:30:50.456 | OWNER: 789 | EMAIL: owner@test.com | ACTION: TEST_PROPERTY | Details: Test action at 2026-02-02T14:30:50.456
```

### In `logs/residentia-app-admin-actions.log`:
```
2026-02-02 14:30:55.789 | ADMIN: 1 | EMAIL: admin@test.com | ACTION: TEST_APPROVAL | Target: TestEntity (ID: 12345)
```

## Real-Time Monitoring

Open a new terminal and watch logs in real-time:

```powershell
# Watch all logs
Get-Content logs\residentia-app.log -Wait -Tail 50

# Watch client actions
Get-Content logs\residentia-app-client-actions.log -Wait -Tail 20
```

Then make requests from another terminal and see logs appear instantly!

## Using Browser/Postman

You can also test using a browser or Postman:

### Browser (GET requests only):
- http://localhost:8888/api/test/logging
- http://localhost:8888/api/test/logging/status
- http://localhost:8888/api/test/logging/error
- http://localhost:8888/api/test/logging/debug
- http://localhost:8888/api/test/logging/all

### Postman (for POST requests):
1. Create a new POST request
2. URL: `http://localhost:8888/api/test/logging/client`
3. Headers: `Content-Type: application/json`
4. Body (raw JSON):
```json
{
  "userId": 123,
  "email": "client@test.com",
  "action": "TEST_BOOKING"
}
```

## Verify Automatic Logging (AOP)

Every controller method is automatically logged! Try any existing endpoint:

```powershell
# If you have a bookings endpoint
curl http://localhost:8888/api/client/bookings `
  -H "Authorization: Bearer your-token-here"
```

Check the logs - you'll see automatic logging like:
```
📍 CLIENT Request: GET /api/client/bookings | Method: ClientBookingController.getClientBookings | User: john@example.com
✅ CLIENT Action Completed: ClientBookingController.getClientBookings | User: john@example.com | Duration: 123ms
```

## Troubleshooting

### No logs directory?
- Make sure the application started successfully
- Check console for any errors
- The logs directory is created automatically on first log write

### Empty log files?
- Make sure you made at least one request to the test endpoints
- Check that the application is running
- Verify `logback-spring.xml` is in `src/main/resources/`

### Can't see DEBUG logs?
- Check `application.yml` - logging level must be set to DEBUG:
```yaml
logging:
  level:
    com.residentia: DEBUG
```

### Logs not rotating?
- Don't worry, rotation happens automatically
- Files rotate when they reach 10MB or at midnight
- Old logs go to `logs/archived/` as `.gz` files

## Success Checklist

✅ Application starts without errors  
✅ `logs/` directory is created  
✅ Log files are created after making requests  
✅ Client actions appear in `residentia-app-client-actions.log`  
✅ Owner actions appear in `residentia-app-owner-actions.log`  
✅ Admin actions appear in `residentia-app-admin-actions.log`  
✅ Errors appear in `residentia-app-error.log`  
✅ All logs appear in `residentia-app.log`  
✅ Console shows log messages  
✅ Automatic AOP logging works for all controllers  

## Next Steps

Once you've verified logging works:

1. **Remove the test controller** (optional):
   - Delete `LoggingTestController.java` before production
   - Or keep it for future testing

2. **Add logging to your other controllers**:
   - See `LOGGING_QUICKSTART.md` for examples
   - Use `@Slf4j` annotation
   - Inject `ActionLogger`

3. **Monitor logs in production**:
   - Set up log aggregation (e.g., ELK stack)
   - Set appropriate log levels (INFO for production)
   - Monitor the `archived/` folder size

4. **Customize as needed**:
   - Edit `logback-spring.xml` for different rotation policies
   - Adjust log levels in `application.yml`
   - Add more specific loggers as needed

---

**Your logging system is fully configured and ready for testing!** 🎉
