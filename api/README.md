# FestMap API

Ce répertoire contient l'implémentation de l'API REST pour l'application FestMap, développée avec Spring Boot. Elle gère la logique métier et l'accès aux données des festivals.

## Technologies utilisées

*   Java 17
*   Spring Boot
*   Maven
*   PostgreSQL (via Docker Compose)

## Configuration

La configuration de l'application (ports, base de données) est centralisée dans un fichier `.env` qui doit être placé à la racine du projet (`FestMap/`).

Pour commencer, copiez le fichier d'exemple et personnalisez-le si besoin :
```bash
cp .env.example .env
```
Ce fichier est utilisé par Docker Compose pour configurer les services et peut également être utilisé pour une exécution locale.

## Démarrage rapide

### Prérequis

Assurez-vous d'avoir Java Development Kit (JDK) 17+ et Maven installés sur votre machine, ou Docker et Docker Compose si vous préférez exécuter l'application conteneurisée.

### Exécuter avec Docker Compose (recommandé)

Après avoir créé votre fichier `.env`, vous pouvez démarrer l'API et ses dépendances depuis la racine du projet :

```bash
docker-compose up --build api
```

L'API sera accessible sur le port `API_PORT` défini dans votre `.env` (par défaut: `http://localhost:8080`).

### Exécuter localement avec Maven

1.  Assurez-vous que les variables de votre fichier `.env` correspondent à votre instance de base de données locale.
2.  Chargez les variables d'environnement (par exemple, avec `source .env` si votre shell le supporte).
3.  Naviguez vers le répertoire `api` :
    ```bash
    cd api
    ```

4.  **Construire le projet :**
    ```bash
    ./mvnw clean install
    ```

5.  **Exécuter l'application Spring Boot :**
    ```bash
    ./mvnw spring-boot:run
    ```

L'API sera accessible sur le port `API_PORT` défini dans votre environnement.

## Qualité du Code et Tests

Ce projet utilise plusieurs plugins Maven pour garantir un haut niveau de qualité de code.

### Lancer les tests

Pour exécuter les tests unitaires et d'intégration :

```bash
cd api
./mvnw test
```

### Cycle de vie complet de vérification

Pour lancer le cycle de vie complet (tests, rapport de couverture, vérification du style de code et du seuil de couverture), utilisez la commande `verify`. C'est la commande à utiliser en intégration continue.

```bash
cd api
./mvnw verify
```

Cette commande va automatiquement :
1.  Compiler le code.
2.  Exécuter tous les tests.
3.  Analyser la couverture de code avec **JaCoCo**. Le rapport est généré dans `target/site/jacoco/index.html`.
4.  Vérifier que le seuil de couverture de 70% est atteint.
5.  Valider le style de code avec **Checkstyle**.

Si l'une de ces étapes échoue, le build sera interrompu.


## Endpoints de l'API

Voici les principaux endpoints disponibles :

*   `GET /api/health` : Vérifie l'état de santé de l'application et de la connexion à la base de données.
*   `GET /api/festivals` : Récupère la liste de tous les festivals.
