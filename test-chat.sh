#!/bin/bash

echo "🧪 Testing Chat System..."

# Wait for backend to start
echo "⏳ Waiting for backend to start..."
sleep 10

# Test 1: Register a test user
echo "📝 Test 1: Registering test user..."
REGISTER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "chatuser@test.com",
    "password": "123456",
    "fullName": "Chat Test User",
    "phone": "0123456789",
    "username": "chatuser"
  }')

echo "Register response: $REGISTER_RESPONSE"

# Test 2: Login
echo "🔐 Test 2: Logging in..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "chatuser@test.com",
    "password": "123456"
  }')

echo "Login response: $LOGIN_RESPONSE"

# Extract token
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
echo "Token: $TOKEN"

# Test 3: Create conversation
echo "💬 Test 3: Creating conversation..."
CONVERSATION_RESPONSE=$(curl -s -X POST http://localhost:8080/api/chat/conversations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "hostId": 3,
    "houseId": 24
  }')

echo "Conversation response: $CONVERSATION_RESPONSE"

# Extract conversation ID
CONVERSATION_ID=$(echo $CONVERSATION_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
echo "Conversation ID: $CONVERSATION_ID"

# Test 4: Send message
echo "📤 Test 4: Sending message..."
MESSAGE_RESPONSE=$(curl -s -X POST http://localhost:8080/api/chat/messages \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "conversationId": '$CONVERSATION_ID',
    "receiverId": 3,
    "houseId": 24,
    "content": "Hello from test script!",
    "messageType": "TEXT"
  }')

echo "Message response: $MESSAGE_RESPONSE"

# Test 5: Get messages
echo "📥 Test 5: Getting messages..."
MESSAGES_RESPONSE=$(curl -s -X GET "http://localhost:8080/api/chat/conversations/$CONVERSATION_ID/messages" \
  -H "Authorization: Bearer $TOKEN")

echo "Messages response: $MESSAGES_RESPONSE"

echo "✅ Chat system test completed!" 