# 🚀 FestMap (Java + Angular + Postgres)

[![CI - main](https://github.com/Supzero-0/FestMap/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/Supzero-0/FestMap/actions/workflows/ci.yml)

FestMap est une application Micro-SaaS permettant de découvrir, filtrer et planifier les festivals musicaux autour de soi grâce à une carte interactive et personnalisée.
Architecture : **Spring Boot 3 (Java 21) + Angular 20 + PostgreSQL 16**.


## 📂 Structure du projet

```
FestMap/
├── api/ # Backend (Spring Boot)
│   ├── Dockerfile
│   └── src/
├── client/ # Frontend (Angular)
│   ├── Dockerfile
│   └── src/
├── .github/ # Templates issues/PR
│   ├── ISSUE_TEMPLATE/
│   └── pull_request_template.md
├── .lefthook.yml      # Hooks Git (lint/format/test)
├── compose.yml        # Docker Compose multi-services
├── .env.example # Variables d’environnement
└── README.md
```


## ⚙️ Prérequis
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (v4+)  
- [Docker Compose](https://docs.docker.com/compose/) v2  
- (Optionnel) Java 21 + Node 20 si tu veux lancer hors Docker  
- (Optionnel) Lefthook installé localement pour profiter des hooks Git

## ▶️ Démarrage rapide
1. Clone le repo :
   ```bash
   git clone https://github.com/Supzero-0/FestMap
   cd FestMap
    ```

2. Configure tes variables :
    ```
    cp .env.example .env
    ```

3. Lance l’application :
    ```
    docker compose up -d --build
    ```

4. Vérifie :
- Frontend → http://localhost:4200
- API Health → http://localhost:8080/api/health

## 📖 Documentation API
La documentation de l'API est générée automatiquement avec **OpenAPI (Swagger)**.
Elle est interactive et permet de visualiser et tester chaque endpoint.

- **URL d'accès** → http://localhost:8080/swagger-ui.html

## 🧪 Health check attendu

Succès :
```
{ "status": "ok", "message": "API connected to database!" }
```

Erreur :
```
{ "status": "error", "message": "Database connection failed" }
```

## 🛠️ Outils de qualité

### Backend (Java)
- Google Java Format (formatage)
- Checkstyle (règles de style)
- JUnit (tests unitaires)

### Frontend (Angular)
- ESLint (linting)
- Prettier (formatage)
- Karma/Jasmine (tests unitaires)

### Git hooks
- pre-commit → lint + format check (front & back)
- commit-msg → vérifie le format du message (Commitlint, Conventional Commits)
- pre-push → exécute les tests unitaires
- post-commit → notifications (optionnel)

## 🔄 Intégration continue (CI)

GitHub Actions vérifie automatiquement :
1. Backend → build + lint + tests
2. Frontend → lint + format + build + tests
3. Docker → lint des Dockerfiles (hadolint)

➡️ Le merge est bloqué si la qualité échoue ✅

## 📊 Diagramme de déploiement
Consultez le [diagramme de déploiement](DEPLOYMENT_DIAGRAM.md) pour une vue d'ensemble de l'infrastructure.

## 🚀 Production

### URLs des applications

Voici les URLs où les applications sont déployées.

*   **Frontend (Vercel):** https://fest-map-ml.vercel.app/
*   **API (Render):** https://festmap.onrender.com/

### Variables d'environnement nécessaires

Le déploiement en production nécessite la configuration de variables d'environnement spécifiques sur Vercel et Render. Celles-ci incluent généralement :

*   `DATABASE_URL`: L'URL de connexion à la base de données PostgreSQL.
*   `JWT_SECRET`: Une clé secrète pour la signature des jetons JWT.
*   `FRONTEND_URL`: L'URL de l'application frontend (pour la configuration CORS côté API).
*   Et toute autre clé API ou secret nécessaire aux services.

### Déploiement

Le déploiement en production est automatisé via **GitHub Actions**.

*   Lors d'un push sur la branche `main` (ou d'une Pull Request mergée), les workflows CI/CD sont déclenchés.
*   L'application **Frontend** est déployée sur **Vercel**.
*   L'application **Backend** est déployée sur **Render**.

## 🔐 Authentification

### Comment s'inscrire et se connecter

1.  **Inscription (Register):** Les utilisateurs peuvent créer un nouveau compte en fournissant une adresse e-mail et un mot de passe via l'interface du frontend. Le frontend envoie ces informations à l'API pour créer un nouvel utilisateur dans la base de données.
2.  **Connexion (Login):** Une fois inscrit, l'utilisateur peut se connecter en utilisant son adresse e-mail et son mot de passe. L'API valide ces identifiants et, en cas de succès, retourne un jeton d'authentification (JWT). Ce jeton est ensuite stocké côté client et utilisé pour toutes les requêtes authentifiées ultérieures à l'API.

## 📌 Workflow collaboratif

- Issues :
    - 🐞 Bug report : .github/ISSUE_TEMPLATE/bug_report.md
    - ✨ Feature request : .github/ISSUE_TEMPLATE/feature_request.md
- Pull requests :
    - .github/pull_request_template.md
- Conventions de commit :
    - Format : feat:, fix:, chore:, refactor:, etc. (Conventional Commits)

## 🧹 Commandes utiles

```bash
# Lancer l’app
docker compose up -d --build

# Logs
docker compose logs -f api

# Arrêt
docker compose down

# Reset complet (y compris DB)
docker compose down -v

# Hooks Lefthook (manuel)
npx lefthook run pre-commit
```

## 📜 Licence

Projet pédagogique – libre de réutilisation.