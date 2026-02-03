# Quick Safety Checklist for AWS RDS Deployment

## ✅ PRE-DEPLOYMENT CHECKLIST

### Before You Start:
- [ ] AWS account created and logged in
- [ ] RDS MySQL instance created and status is "Available"
- [ ] Security Group configured (port 3306 open)
- [ ] RDS endpoint and credentials noted down
- [ ] MySQL client installed on your computer

### Safety Measures:
- [ ] Local database backed up
- [ ] Backup file verified (can be opened)
- [ ] Backup stored in safe location
- [ ] Have rollback plan ready

---

## 🚀 SAFE DEPLOYMENT STEPS

### Step 1: Run the Safe Deployment Script
```powershell
cd server\residentia-backend
.\SAFE_DEPLOY_RDS.ps1
```

This script will:
1. ✅ Backup your local database
2. ✅ Test RDS connection
3. ✅ Import schema to RDS
4. ✅ Optionally migrate data
5. ✅ Create environment scripts
6. ✅ Create rollback script

### Step 2: Connect to RDS (Temporary Test)
```powershell
# Load RDS environment variables
.\backups\[timestamp]\set_rds_env.ps1

# Start application
mvn spring-boot:run
```

### Step 3: Test Your Application
- [ ] Application starts without errors
- [ ] Login works
- [ ] Registration works
- [ ] Properties display correctly
- [ ] CRUD operations work
- [ ] Bookings work
- [ ] Images load properly

### Step 4A: If Everything Works ✅
Keep using RDS! Your environment variables will connect to RDS.

### Step 4B: If Something Fails ❌
**Immediate Rollback:**
```powershell
# Stop the application (Ctrl+C)

# Run rollback script
.\backups\[timestamp]\rollback_to_local.ps1

# Restart application (now using local database)
mvn spring-boot:run
```

---

## 🔄 SWITCHING BETWEEN LOCAL AND RDS

### Use RDS (Production):
```powershell
# Set environment variables
$env:SPRING_DATASOURCE_URL="jdbc:mysql://your-rds-endpoint:3306/residentia_db?useSSL=true&serverTimezone=UTC"
$env:SPRING_DATASOURCE_USERNAME="admin"
$env:SPRING_DATASOURCE_PASSWORD="your_password"

mvn spring-boot:run
```

### Use Local (Development):
```powershell
# Clear environment variables
Remove-Item Env:SPRING_DATASOURCE_URL -ErrorAction SilentlyContinue
Remove-Item Env:SPRING_DATASOURCE_USERNAME -ErrorAction SilentlyContinue
Remove-Item Env:SPRING_DATASOURCE_PASSWORD -ErrorAction SilentlyContinue

mvn spring-boot:run
```

---

## 🛡️ SAFETY GUARANTEES

### What's Protected:
✅ **Local Database**: Never modified by RDS deployment  
✅ **Application Code**: Zero changes required  
✅ **Configuration**: Uses environment variables only  
✅ **Data**: Backed up before any changes  
✅ **Reversible**: Can rollback anytime  

### What Changes:
- Environment variables (temporary, session-only)
- Where data is stored (local → cloud)
- Nothing permanent unless you make it permanent

---

## 📊 COMPARISON

| Aspect | Local MySQL | AWS RDS |
|--------|-------------|---------|
| **Location** | Your computer | AWS Cloud |
| **Code Changes** | None | None |
| **Data Safety** | Backup required | Auto-backups |
| **Accessibility** | Only local | Internet accessible |
| **Scalability** | Limited | Highly scalable |
| **Cost** | Free | $0 (Free Tier) or ~$30/month |

---

## ⚠️ IMPORTANT NOTES

1. **Environment Variables are Temporary**
   - Only valid in current PowerShell session
   - Close terminal = back to local
   - Make permanent only when you're sure

2. **Local Database Always Available**
   - RDS doesn't touch your local database
   - Can use both simultaneously
   - Switch anytime with environment variables

3. **Test Thoroughly Before Production**
   - Test all features
   - Check data integrity
   - Verify performance
   - Monitor for 24-48 hours

4. **Keep Backups**
   - Backup before deployment
   - Keep multiple backups
   - Test restore procedure
   - Document backup locations

---

## 🆘 TROUBLESHOOTING

### Problem: Can't connect to RDS
**Solution:**
1. Check Security Group (port 3306 open for your IP)
2. Verify Public Access enabled
3. Confirm correct endpoint and credentials
4. Test with: `telnet your-rds-endpoint 3306`

### Problem: Application won't start
**Solution:**
1. Check application logs for errors
2. Verify environment variables set correctly
3. Test RDS connection with MySQL client
4. Rollback to local if needed

### Problem: Data missing after migration
**Solution:**
1. Rollback to local database immediately
2. Restore from backup
3. Check migration logs
4. Try migration again with verbose logging

### Problem: Want to go back to local
**Solution:**
```powershell
# Just clear environment variables
Remove-Item Env:SPRING_DATASOURCE_URL -ErrorAction SilentlyContinue
Remove-Item Env:SPRING_DATASOURCE_USERNAME -ErrorAction SilentlyContinue
Remove-Item Env:SPRING_DATASOURCE_PASSWORD -ErrorAction SilentlyContinue

# Restart application
mvn spring-boot:run
```

---

## 📞 SUPPORT RESOURCES

- **AWS RDS Documentation**: https://docs.aws.amazon.com/rds/
- **Spring Boot + MySQL**: https://spring.io/guides/gs/accessing-data-mysql/
- **MySQL Documentation**: https://dev.mysql.com/doc/

---

## ✅ POST-DEPLOYMENT CHECKLIST

After successful deployment:
- [ ] All features tested and working
- [ ] Data verified correct
- [ ] Performance acceptable
- [ ] Monitoring enabled in AWS Console
- [ ] Backup schedule configured
- [ ] Cost alerts set up
- [ ] Documentation updated
- [ ] Team informed of changes

---

**Remember: This is a SAFE deployment. You can always go back to local database! 🛡️**
