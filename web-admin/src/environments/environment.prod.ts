// Production environment (Docker / Kubernetes)
// Nginx serves the Angular app AND proxies /api/* to the backend service.
// So we use an empty base URL — all API calls become relative /api/... paths
// that Nginx picks up and forwards to backend-svc:8080.
export const environment = {
  production: true,
  apiBaseUrl: ''  // Relative — Nginx proxy handles routing to backend
};
