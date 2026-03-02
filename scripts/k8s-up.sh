#!/bin/bash
# ═══════════════════════════════════════════════════════════════
# SAySmile K8s Redeploy Script
# ═══════════════════════════════════════════════════════════════

set -e

# Always run from the project root
cd "$(dirname "$0")/.."

#echo "🛑 Stopping all deployments..."
#sg docker -c "kubectl delete deployment,statefulset --all -n saysmile"

#echo "🗑️  Deleting all data volumes (PVCs)..."
#sg docker -c "kubectl delete pvc --all -n saysmile"


echo "🚀 Building backend (latest)..."
sg docker -c "docker build -t saysmile/backend:latest ./backend/ -q"

echo "🚀 Building web-admin (latest)..."
sg docker -c "docker build -t saysmile/web-admin:latest ./web-admin/ -q"

echo "📦 Loading images into Minikube..."
sg docker -c "minikube image load saysmile/backend:latest"
sg docker -c "minikube image load saysmile/web-admin:latest"

echo "🚢 Applying Kubernetes manifests..."
sg docker -c "kubectl apply -f k8s/"

echo "♻️ Restarting deployments..."
sg docker -c "kubectl rollout restart deployment/backend -n saysmile"
sg docker -c "kubectl rollout restart deployment/web-admin -n saysmile"

echo "⏳ Waiting for rollout..."
sg docker -c "kubectl rollout status deployment/backend -n saysmile --timeout=120s"
sg docker -c "kubectl rollout status deployment/web-admin -n saysmile --timeout=120s"

echo "✅ Redeploy complete."
