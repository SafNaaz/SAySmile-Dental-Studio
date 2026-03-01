#!/bin/bash
# ═══════════════════════════════════════════════════════════════
# SAySmile Public Access Script (Cloudflare Tunnel)
# ═══════════════════════════════════════════════════════════════

set -e

# 1. Check for cloudflared
if ! command -v cloudflared &> /dev/null; then
  echo "⚠️ cloudflared not found. Installing..."
  curl -L --output cloudflared.deb https://github.com/cloudflare/cloudflared/releases/latest/download/cloudflared-linux-amd64.deb
  sudo dpkg -i cloudflared.deb
  rm cloudflared.deb
fi

# 2. Check Minikube Status
if ! sg docker -c "minikube status | grep -q 'Running'"; then
  echo "⚠️ Minikube is not running! Starting it..."
  sg docker -c "minikube start --driver=docker"
fi

echo "🚢 Ensuring services are up..."
sg docker -c "kubectl apply -f k8s/"

# 3. Check for Minikube Tunnel
# We check if we can reach the ingress IP
echo "🔍 Checking for Active Minikube Tunnel..."
if ! ping -c 1 -W 1 192.168.49.2 &> /dev/null; then
  echo "****************************************************************"
  echo "⚠️  CRITICAL: Minikube Tunnel is NOT running."
  echo "****************************************************************"
  echo "Please open a NEW terminal and run the following command:"
  echo ""
  echo "    sudo minikube tunnel"
  echo ""
  echo "Then come back here and press [ENTER]."
  read -p ""
fi

# 4. Get the reachable URL for the Web Admin
echo "🔗 Locating Web Admin Service..."
ADMIN_URL=$(sg docker -c "minikube service web-admin-svc -n saysmile --url" | grep http | head -1)

if [ -z "$ADMIN_URL" ]; then
  echo "❌ Error: Could not find web-admin-svc. Is it running?"
  exit 1
fi

echo "🚀 Starting Cloudflare Public Tunnel for: $ADMIN_URL"
echo "----------------------------------------------------------------"
echo "Look for the URL starting with 'https://' and '.trycloudflare.com'"
echo "That will be your public link for the entire SAySmile app!"
echo "----------------------------------------------------------------"

cloudflared tunnel --url "$ADMIN_URL"
