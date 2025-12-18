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
