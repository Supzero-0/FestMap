# FestMap API

Ce répertoire contient l'implémentation de l'API REST pour l'application FestMap, développée avec Spring Boot. Elle gère la logique métier et l'accès aux données des festivals.

## Technologies utilisées

*   Java 17
*   Spring Boot
*   Maven
*   PostgreSQL (via Docker Compose)

## Démarrage rapide

### Prérequis

Assurez-vous d'avoir Java Development Kit (JDK) 17+ et Maven installés sur votre machine, ou Docker et Docker Compose si vous préférez exécuter l'application conteneurisée.

### Exécuter avec Docker Compose (recommandé)

Depuis le répertoire racine du projet (`FestMap/`), vous pouvez démarrer l'API et ses dépendances (base de données) avec Docker Compose :

```bash
docker-compose up --build api
```

L'API sera accessible sur `http://localhost:8080`.

### Exécuter localement avec Maven

1.  **Naviguez vers le répertoire `api` :**
    ```bash
    cd api
    ```

2.  **Construire le projet :**
    ```bash
    ./mvnw clean install
    ```

3.  **Exécuter l'application Spring Boot :**
    ```bash
    ./mvnw spring-boot:run
    ```

L'API sera accessible sur `http://localhost:8080`.

## Compilation et Tests

Pour compiler le projet et exécuter les tests unitaires :

```bash
cd api
./mvnw clean install
```

Pour exécuter uniquement les tests :

```bash
cd api
./mvnw test
```

## Formatage et Linting

Ce projet adhère aux conventions de code Java standard. Si des plugins spécifiques de formatage ou d'analyse statique (comme Checkstyle ou Spotless) sont configurés dans le `pom.xml`, vous pouvez utiliser les commandes suivantes (à adapter selon les plugins réellement configurés) :

Pour appliquer le formatage du code :
```bash
./mvnw spotless:apply # Exemple si Spotless est utilisé
```

Pour vérifier la conformité au style et les règles de linting :
```bash
./mvnw checkstyle:check # Exemple si Checkstyle est utilisé
```

## Endpoints de l'API

Voici les principaux endpoints disponibles :

*   `GET /api/health` : Vérifie l'état de santé de l'application et de la connexion à la base de données.
*   `GET /api/festivals` : Récupère la liste de tous les festivals.
