# SAySmile Dental Studio - Project Blueprint

A comprehensive internal control and patient-facing clinic management system for SAySmile Dental Studio.

## 👥 Roles & Access (RBAC)
*   **Super Admin (You & Your Wife)**: Complete access to all data, financial records, inventory, analytics, and clinic configurations.
*   **Doctor**: Access to their own daily schedule, patient dental records/history, and ability to request inventory items. (View restricted to medical/appointment data).
*   **Front Desk/Reception**: Access to patient check-ins, appointment scheduling, billing generation, and simple follow-ups.
*   **Inventory/HR**: Access to supply stock, ordering statuses, employee attendance, and timesheets.
*   **Patient**: Access via Web/Mobile to book appointments, view digital prescriptions, and track past bills.

## 📊 Analytics Dashboard & Insights
The system will feature a dedicated Analytics module accessible **only by Super Admins**. 
*   **Patient Flow Analysis**: Peak days, appointment cancellation rates, new vs. returning patients.
*   **Pathology Trends**: Track most common dental issues (e.g., Caries, Gingivitis, Wisdom Tooth Extraction).
*   **Seasonality Mapping**: Which month has more of a certain disease (e.g., sensitivity spikes in winter).
*   **Revenue & Inventory Velocity**: Profitability per procedure, most consumed supplies (gloves, composites).

## 🏗️ Technical Stack
*   **Database**: PostgreSQL
*   **Caching & Concurrency**: Redis
*   **Authentication Directory**: OpenLDAP (for internal clinic staff)
*   **Backend Api**: Spring Boot 3 (Java/Kotlin)
*   **Web Portal**: Angular (Admin/Clinic Dashboards)
*   **Mobile App**: React Native (Patient & Doctor Views)
*   **Integrations**: WhatsApp Business API
*   **Deployments**: Docker & Kubernetes

---

## 🚀 Directory Structure & Sub-Folders
*   `backend/` - Spring Boot API gateway
*   `web-admin/` - Angular Web Application
*   `mobile-app/` - React Native application
*   `landing-page/` - GitHub Pages site
*   `k8s/` - Kubernetes manifests
*   `docker-compose.yml` - Local database and service orchestrator

## 🛠 Getting Started

### 1. Local Infrastructure (Postgres, Redis, LDAP)
Run the following from the root directory to spin up databases and auth layer locally:
```bash
docker compose up -d
```
*   **Postgres**: `localhost:5432` / `saysmile_admin:saysmile_password`
*   **Redis**: `localhost:6379`
*   **phpLDAPadmin**: `http://localhost:6443` (Login using OpenLDAP credentials specified in docker-compose.yml)
