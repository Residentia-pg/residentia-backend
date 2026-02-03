#!/bin/bash
# AWS RDS Database Setup Script
# This script helps you set up your database schema on AWS RDS

echo "============================================"
echo "AWS RDS Database Setup for Residentia"
echo "============================================"
echo ""

# Check if mysql client is installed
if ! command -v mysql &> /dev/null; then
    echo "❌ MySQL client is not installed!"
    echo "Please install it first:"
    echo "  Ubuntu/Debian: sudo apt-get install mysql-client"
    echo "  Mac: brew install mysql-client"
    echo "  Windows: Download from https://dev.mysql.com/downloads/mysql/"
    exit 1
fi

# Get RDS connection details
echo "Please enter your AWS RDS connection details:"
echo ""
read -p "RDS Endpoint (e.g., residentia-db.xxx.us-east-1.rds.amazonaws.com): " RDS_ENDPOINT
read -p "Port [3306]: " RDS_PORT
RDS_PORT=${RDS_PORT:-3306}
read -p "Username [admin]: " RDS_USER
RDS_USER=${RDS_USER:-admin}
read -sp "Password: " RDS_PASSWORD
echo ""
read -p "Database Name [residentia_db]: " RDS_DATABASE
RDS_DATABASE=${RDS_DATABASE:-residentia_db}

echo ""
echo "============================================"
echo "Testing Connection..."
echo "============================================"

# Test connection
mysql -h $RDS_ENDPOINT -P $RDS_PORT -u $RDS_USER -p$RDS_PASSWORD -e "SELECT 'Connected!' AS Status;" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ Connection successful!"
    echo ""
    
    # Ask if user wants to import schema
    read -p "Do you want to import the database schema? (y/n): " IMPORT_SCHEMA
    
    if [ "$IMPORT_SCHEMA" = "y" ] || [ "$IMPORT_SCHEMA" = "Y" ]; then
        SCHEMA_FILE="src/main/resources/schema.sql"
        
        if [ -f "$SCHEMA_FILE" ]; then
            echo ""
            echo "============================================"
            echo "Importing Schema..."
            echo "============================================"
            
            mysql -h $RDS_ENDPOINT -P $RDS_PORT -u $RDS_USER -p$RDS_PASSWORD < $SCHEMA_FILE
            
            if [ $? -eq 0 ]; then
                echo "✅ Schema imported successfully!"
                
                # Verify tables
                echo ""
                echo "Verifying tables..."
                mysql -h $RDS_ENDPOINT -P $RDS_PORT -u $RDS_USER -p$RDS_PASSWORD -e "USE $RDS_DATABASE; SHOW TABLES;"
                
            else
                echo "❌ Failed to import schema!"
            fi
        else
            echo "❌ Schema file not found: $SCHEMA_FILE"
        fi
    fi
    
    # Generate environment variables
    echo ""
    echo "============================================"
    echo "Environment Variables"
    echo "============================================"
    echo ""
    echo "Copy these to your environment:"
    echo ""
    echo "# Linux/Mac:"
    echo "export SPRING_DATASOURCE_URL=\"jdbc:mysql://$RDS_ENDPOINT:$RDS_PORT/$RDS_DATABASE?useSSL=true&serverTimezone=UTC\""
    echo "export SPRING_DATASOURCE_USERNAME=\"$RDS_USER\""
    echo "export SPRING_DATASOURCE_PASSWORD=\"$RDS_PASSWORD\""
    echo ""
    echo "# Windows PowerShell:"
    echo "\$env:SPRING_DATASOURCE_URL=\"jdbc:mysql://$RDS_ENDPOINT:$RDS_PORT/$RDS_DATABASE?useSSL=true&serverTimezone=UTC\""
    echo "\$env:SPRING_DATASOURCE_USERNAME=\"$RDS_USER\""
    echo "\$env:SPRING_DATASOURCE_PASSWORD=\"$RDS_PASSWORD\""
    echo ""
    echo "============================================"
    echo "✅ Setup Complete!"
    echo "============================================"
    
else
    echo "❌ Connection failed!"
    echo ""
    echo "Troubleshooting:"
    echo "1. Check your RDS endpoint is correct"
    echo "2. Verify username and password"
    echo "3. Ensure Security Group allows your IP (port 3306)"
    echo "4. Check Public Access is enabled in RDS settings"
    exit 1
fi
