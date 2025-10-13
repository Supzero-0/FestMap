# 🚀 FestMap (Java + Angular + Postgres)

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

## 📌 Workflow collaboratif

- Issues :
    - 🐞 Bug report : .github/ISSUE_TEMPLATE/bug_report.md
    - ✨ Feature request : .github/ISSUE_TEMPLATE/feature_request.md
- Pull requests :
    - .github/pull_request_template.md
- Conventions de commit :
    - Format : feat:, fix:, chore:, refactor:, etc. (Conventional Commits)

## 🧹 Commandes utiles

```
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