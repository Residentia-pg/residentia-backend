# Fix RDS Connection Error 2003 (10060)

## Problem
Your RDS instance is blocking connections from your computer.

## Quick Fix Steps

### Step 1: Get Your Public IP
Run this command to see your IP:
```powershell
(Invoke-WebRequest -Uri "https://api.ipify.org").Content
```

**Copy this IP address!**

---

### Step 2: Fix Security Group in AWS

#### 2A. Go to RDS Console
1. Open: https://console.aws.amazon.com/rds/
2. Click **"Databases"** in left menu
3. Click on **"database-1"**

#### 2B. Find Security Group
Look under **"Connectivity & security"** tab:
- Find: **VPC security groups**
- Click the security group link (e.g., `sg-xxxxx`)

#### 2C. Edit Inbound Rules
1. Click **"Edit inbound rules"** button
2. Click **"Add rule"**
3. Configure:
   - **Type**: `MySQL/Aurora` (auto-fills port 3306)
   - **Source**: `My IP` (or paste your IP from Step 1)
   - **Description**: `My computer access`
4. Click **"Save rules"**

---

### Step 3: Check Public Access

Back in RDS Console → database-1:

Look for **"Publicly accessible"**:
- If **NO** → You need to modify this:
  1. Click **"Modify"** button (top right)
  2. Scroll to **"Connectivity"**
  3. Set **"Public access"** to **Yes**
  4. Click **"Continue"** → **"Apply immediately"** → **"Modify DB instance"**
  5. Wait 2-3 minutes for changes

---

### Step 4: Test Connection Again

```powershell
mysql -h database-1.c7mm0eumyni6.ca-central-1.rds.amazonaws.com -P 3306 -u admin -p
```

**Expected Result**: Should prompt for password and connect successfully!

---

## Alternative: Allow All IPs (Less Secure but Easier)

If you want to allow access from anywhere:

**Security Group Inbound Rule:**
- **Type**: MySQL/Aurora
- **Port**: 3306
- **Source**: `0.0.0.0/0` (anywhere IPv4)
- **Description**: `Public access`

⚠️ **Warning**: This allows anyone to attempt connection. Make sure you have a strong password!

---

## Troubleshooting

### Still Can't Connect?

**Check VPC Settings:**
1. RDS Console → database-1
2. Note the **VPC ID**
3. Go to VPC Console: https://console.aws.amazon.com/vpc/
4. Check VPC has:
   - Internet Gateway attached
   - Route table with `0.0.0.0/0 → igw-xxxxx` route
   - Subnet is public (associated with public route table)

**Check Network ACLs:**
1. VPC Console → Network ACLs
2. Find ACL for your subnet
3. Ensure inbound/outbound rules allow port 3306

---

## Quick Checklist

- [ ] Security Group allows port 3306 from your IP
- [ ] RDS "Publicly accessible" = Yes
- [ ] RDS Status = "Available"
- [ ] VPC has Internet Gateway
- [ ] Subnet is public
- [ ] Network ACL allows traffic
- [ ] Your firewall/antivirus not blocking MySQL

---

## After Fixing, Run Deployment Script

```powershell
.\SAFE_DEPLOY_RDS.ps1
```

This will:
1. Backup local database
2. Connect to RDS
3. Import schema
4. Migrate data
5. Configure application
