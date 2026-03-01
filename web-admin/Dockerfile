# ─── Stage 1: Build Angular App ───────────────────────────────────────────────
FROM node:20-alpine AS builder
WORKDIR /app

# Copy package files first for layer caching
COPY package.json package-lock.json ./
RUN npm ci --legacy-peer-deps

# Copy source and build for production
COPY . .
RUN npx ng build --configuration production --output-path=/dist

# ─── Stage 2: Serve with Nginx ────────────────────────────────────────────────
FROM nginx:1.27-alpine AS runtime

# Remove default nginx config
RUN rm /etc/nginx/conf.d/default.conf

# Copy our custom config (uses envsubst for BACKEND_HOST)
COPY nginx.conf /etc/nginx/templates/default.conf.template

# Copy built Angular app from builder stage
COPY --from=builder /dist/browser /usr/share/nginx/html

EXPOSE 80

# envsubst replaces ${BACKEND_HOST} in the nginx template at container start
CMD ["/bin/sh", "-c", "envsubst '${BACKEND_HOST}' < /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"]
