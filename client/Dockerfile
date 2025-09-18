# --- Dev stage ---
FROM node:22-alpine AS dev
WORKDIR /app
COPY package*.json ./
RUN --mount=type=cache,target=/root/.npm npm ci
ENV CHOKIDAR_USEPOLLING=true
ENV CHOKIDAR_INTERVAL=300
CMD ["sh","-lc","npm run start --host=0.0.0.0 --port=4200"]

# --- Build stage ---
FROM node:22-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN --mount=type=cache,target=/root/.npm npm ci
COPY . .
RUN npm run build

# --- Prod Runtime stage (Nginx) ---
FROM nginx:1.29-alpine AS prod
COPY --from=builder /app/dist/client/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 4200