#!/bin/bash

echo "🚀 Starting Backend Deployment to Railway..."

# Check if Railway CLI is installed
if ! command -v railway &> /dev/null; then
    echo "❌ Railway CLI not found. Installing..."
    npm install -g @railway/cli
fi

# Login to Railway (if not already logged in)
echo "🔐 Logging in to Railway..."
railway login

# Link to existing project (if not already linked)
if [ ! -f ".railway" ]; then
    echo "🔗 Linking to Railway project..."
    railway link
fi

# Set environment variables
echo "⚙️ Setting environment variables..."
railway variables set SPRING_PROFILES_ACTIVE=prod
railway variables set PORT=8080

# Deploy the application
echo "📦 Deploying to Railway..."
railway up

echo "✅ Deployment completed!"
echo "🌐 Your backend should be available at: https://your-app-name.railway.app"
echo "📊 Check deployment status at: https://railway.app/dashboard"
