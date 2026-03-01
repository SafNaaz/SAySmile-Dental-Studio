// Development environment
// In production (Docker/k8s), Nginx proxies /api/* to the backend, so we use a relative path.
// For local dev (npm start), Angular's proxy config or the full localhost URL is used.
export const environment = {
  production: false,
  // For local dev, your backend runs on localhost:8080 directly
  apiBaseUrl: 'http://localhost:8080'
};
