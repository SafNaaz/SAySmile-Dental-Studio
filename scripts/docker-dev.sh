#!/bin/bash
# ═══════════════════════════════════════════════════════════════
# SAySmile Docker Compose Quick Start
# ═══════════════════════════════════════════════════════════════

set -e

echo "🐳 Stopping existing containers..."
sg docker -c "docker compose down -v"

echo "🚀 Building backend & frontend..."
sg docker -c "docker compose build"

echo "🚢 Starting stack..."
sg docker -c "docker compose up -d"

echo "⏳ Waiting for LDAP (important)..."
sleep 5

echo "🔑 Seeding LDAP database..."
sg docker -c "ldapadd -x -H ldap://localhost:389 -D 'cn=admin,dc=saysmile,dc=local' -w admin -f add_ldap.ldif || true"

echo "✅ Local Docker environment running at: http://localhost:4200 ("admin" UI)"
echo "✅ Backend API running at: http://localhost:8080"
echo "✅ phpLDAPadmin running at: http://localhost:6443"
echo "✅ pgAdmin running at: http://localhost:5050"
