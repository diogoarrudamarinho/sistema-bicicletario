FROM node:18-alpine AS builder

WORKDIR /app

COPY package*.json ./
RUN npm install --ignore-scripts

COPY controllers ./controllers
COPY repositories ./repositories
COPY services ./services
COPY tests ./tests
COPY server.js ./ 

# ========================
# Etapa 2: Runtime (seguro e enxuto)
# ========================
FROM node:18-alpine AS runner

WORKDIR /app

# Criação de usuário seguro
RUN addgroup -S nonroot && adduser -S nonroot -G nonroot

# Copia da etapa anterior
COPY --from=builder /app .

RUN chown -R nonroot:nonroot /app

USER nonroot

EXPOSE 3000

CMD ["node", "server.js"]