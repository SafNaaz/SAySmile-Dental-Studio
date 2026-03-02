# 🦷 SAySmile Dental Studio — System Master Guide 🦷
This document provides a comprehensive overview of the SAySmile infrastructure, monitoring, and deployment workflows.

---

## 1. System Components & URLs

The entire system is running on **Kubernetes (Minikube)** with high availability (2 replicas for web/backend).

### 🌐 Application Services
| Service | Internal Name | Public Local URL | Description |
|---|---|---|---|
| **Web Admin** | `web-admin-svc` | [admin.saysmile.local](http://admin.saysmile.local) | Angular Frontend |
| **Backend API** | `backend-svc` | [api.saysmile.local](http://api.saysmile.local) | Spring Boot REST API |
| **API Health** | - | [api.saysmile.local/actuator/health](http://api.saysmile.local/actuator/health) | Real-time health JSON |

### 📊 Observability (Monitoring Stack)
| Service | Namespace | Default URL | Credentials |
|---|---|---|---|
| **Grafana** | `monitoring` | [monitoring.saysmile.local](http://monitoring.saysmile.local) | `admin` / `admin123` |
| **Prometheus** | `monitoring` | [internal-only] | Metrics scraper |
| **Loki** | `monitoring` | [internal-only] | Log aggregator |
| **Fluent Bit** | `monitoring` | [daemonset] | Log shipper (runs on every node) |

### 🛠️ Infrastructure Dashboards (New Helpers)
| Dashboard | URL | Usage |
|---|---|---|
| **pgAdmin** | [db.saysmile.local](http://db.saysmile.local) | Manage PostgreSQL database |
| **phpLDAPadmin** | [directory.saysmile.local](http://directory.saysmile.local) | Manage User Directory (LDAP) |
| **RedisInsight** | [cache.saysmile.local](http://cache.saysmile.local) | Inspect Redis cache/tokens |

---

## 2. ⚡ Quick Startup & Deployment

### To Start the entire cluster from scratch:
```bash
# 1. Start minikube (if not running)
minikube start --driver=docker

# 2. Enable Ingress
minikube addons enable ingress

# 3. Use my automation script to build and deploy everything
./scripts/k8s-up.sh
```

### To Check Status:
```bash
# View all pods in all relevant namespaces
sg docker -c "kubectl get pods -n saysmile"
sg docker -c "kubectl get pods -n monitoring"
```

---

## 3. 🛠️ Development Workflow Comparison

| Workflow | Best For | Pros | Cons |
|---|---|---|---|
| **Docker Compose** | Fast feature dev | Faster builds, very light | No K8s parity, single replica |
| **Minikube (K8s)** | Final QA / Production testing | 100% Prod-parity, monitoring active | Heavier on RAM/CPU |

**Recommendation:** Develop features using `docker-compose up` first. Before merging, verify using `./scripts/k8s-up.sh`.

---

## 4. 🌍 Public Access (Access from Internet)

To expose this website from your laptop to the public internet, use a **Tunneling Service**:

### Option A: Cloudflare Tunnel (Free & Secure)
1. Install `cloudflared`: `sudo apt install cloudflared`
2. Run `minikube tunnel` (in a separate terminal) to expose the ingress to the host.
3. Run: `cloudflared tunnel --url http://admin.saysmile.local`
4. Cloudflare will give you a public `*.trycloudflare.com` URL.

### Option B: Ngrok
1. Run `minikube tunnel`.
2. Run: `ngrok http admin.saysmile.local:80`

---

## 5. 🗄️ Database & Secrets Management

- **Secrets:** Stored in `k8s/01-secrets.yaml`.
- **Database Backup:** PostgreSQL uses a Persistent Volume Claim (`postgres-pvc`). Even if the pod dies, the data stays.
- **LDAP Seeding:** The user `ayshaldap` is seeded into the Directory service. If you lose LDAP data, run the LDAP import command (refer to `add_ldap.ldif`).

---

## 📝 Troubleshooting & FAQ

**Q: "I get a 403 on health probes"**
A: Check `SecurityConfig.java`. It must permit `/actuator/health/**` without authentication.

**Q: "My changes aren't reflecting"**
A: Use `./scripts/k8s-up.sh`. It forces a rebuild and a `rollout restart` to ensure the pod pulls the fresh image from the local cache.

**Q: "How do I see logs in Grafana?"**
A: Log in to Grafana -> **Explore** -> Source: **Loki** -> Label filter: `{app="backend"}`.


To log in to pgAdmin at http://localhost:5050, use these exact credentials:

Email: safnas@saysmile.com
Password: saysmile_admin
Once you are logged in, to connect to the Database:
Right-click Servers > Register > Server.
General Tab: Name it "SAySmile DB".
Connection Tab:
Host: postgres (use this internal name, not localhost)
Port: 5432
Maintenance database: saysmile_db
Username: saysmile_admin
Password: saysmile_password
Click Save.