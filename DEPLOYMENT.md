# Residentia Deployment Guide (FREE)

## 🎯 Overview
Deploy Backend + Database on Railway (Free) and Frontend on Vercel (Free)

---

## Part 1: Backend Deployment (Railway)

### Step 1: Push Code to GitHub
```bash
cd "c:\Prajwal\Sunbeam\Project\final PG\residentia\residentia"
git init
git add .
git commit -m "Initial commit - Residentia PG Management"
git branch -M main
```

**Go to GitHub.com:**
1. Create new repository: "residentia"
2. Copy the repository URL
3. Run:
```bash
git remote add origin YOUR_GITHUB_URL
git push -u origin main
```

### Step 2: Deploy Backend on Railway

**2.1 Sign Up:**
1. Go to https://railway.app
2. Sign up with GitHub (free)

**2.2 Create New Project:**
1. Click "New Project"
2. Select "Deploy from GitHub repo"
3. Choose your "residentia" repository
4. Railway will auto-detect Java/Maven

**2.3 Add MySQL Database:**
1. In your Railway project, click "+ New"
2. Select "Database" → "MySQL"
3. Railway creates database automatically

**2.4 Configure Environment Variables:**
Click on your backend service → "Variables" → Add these:

```
SPRING_DATASOURCE_URL=mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}
SPRING_DATASOURCE_USERNAME=${MYSQLUSER}
SPRING_DATASOURCE_PASSWORD=${MYSQLPASSWORD}
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SERVER_PORT=${PORT}
JWT_SECRET=your-super-secret-key-for-jwt-token-generation-residentia-application-2024-secure-512-bits-minimum-requirement
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=hspatil222002@gmail.com
SPRING_MAIL_PASSWORD=wrbhlpyrnefljvig
RAZORPAY_KEY_ID=rzp_test_S8v64jsUOHFb42
RAZORPAY_KEY_SECRET=0kQhTEbbiZpkAXgnOMIST0jj
```

**2.5 Deploy:**
1. Railway auto-deploys on every push
2. Wait 3-5 minutes for build
3. Copy your backend URL (e.g., `https://residentia-production.up.railway.app`)

---

## Part 2: Frontend Deployment (Vercel)

### Step 1: Update Frontend API URL

**Edit:** `client/client/src/api/axios.js`

Change:
```javascript
const API_BASE_URL = 'http://localhost:8888';
```

To:
```javascript
const API_BASE_URL = 'https://YOUR-RAILWAY-URL.railway.app';
```

**Commit changes:**
```bash
git add client/client/src/api/axios.js
git commit -m "Update API URL for production"
git push
```

### Step 2: Deploy on Vercel

**2.1 Sign Up:**
1. Go to https://vercel.com
2. Sign up with GitHub (free)

**2.2 Import Project:**
1. Click "Add New..." → "Project"
2. Import your "residentia" repository
3. Configure:
   - **Framework Preset:** Vite
   - **Root Directory:** client/client
   - **Build Command:** npm run build
   - **Output Directory:** dist

**2.3 Environment Variables (Optional):**
```
VITE_API_URL=https://YOUR-RAILWAY-URL.railway.app
```

**2.4 Deploy:**
1. Click "Deploy"
2. Wait 2-3 minutes
3. Your app will be live at: `https://residentia.vercel.app`

---

## Part 3: Final Configuration

### Update CORS in Backend

Railway will redeploy automatically when you push:

**Edit:** `server/residentia-backend/src/main/resources/application.yml`

Add your Vercel URL to allowed origins:
```yaml
spring:
  mvc:
    cors:
      allowed-origins: 
        - http://localhost:5173
        - https://residentia.vercel.app
        - https://*.vercel.app
```

**Commit and push:**
```bash
git add .
git commit -m "Add Vercel URL to CORS"
git push
```

---

## 🎉 You're Live!

**Frontend:** https://residentia.vercel.app  
**Backend API:** https://your-app.railway.app  
**Database:** Managed by Railway  

### Admin Login:
- Email: admin@residentia.com
- Password: admin123

---

## 📊 Free Tier Limits

**Railway:**
- $5 credit/month (enough for small projects)
- 500 hours/month
- 1GB RAM
- MySQL database included

**Vercel:**
- 100GB bandwidth/month
- Unlimited deployments
- Custom domain support

---

## 🔧 Troubleshooting

**Backend not starting?**
- Check Railway logs: Click service → "Logs"
- Verify environment variables are set
- Check database connection

**Frontend can't reach backend?**
- Verify API_BASE_URL in axios.js
- Check CORS configuration
- Ensure Railway backend is running

**Database errors?**
- Railway auto-creates tables on first run
- Check database connection string
- Verify MySQL service is running

---

## 🚀 Next Steps

1. **Custom Domain:** Add your domain in Vercel settings
2. **SSL:** Automatic HTTPS on both platforms
3. **Monitoring:** Check Railway/Vercel dashboards
4. **Logs:** View logs for debugging
5. **Scaling:** Upgrade plans if needed

---

## 📝 Important Notes

- Keep your GitHub repo private (sensitive keys!)
- Never commit actual passwords (use environment variables)
- Railway free tier resets monthly
- Both platforms offer automatic deployments on git push

**Need help?** Check Railway/Vercel documentation or reach out!
