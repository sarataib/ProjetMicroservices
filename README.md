
# Projet MSA – Gestion d’Emprunts avec Kafka

##  Auteur

* **Nom & Prénom** : **Sara Taibi**

---

## Description du Projet

Ce projet met en œuvre une **architecture microservices complète** pour la gestion d’une bibliothèque, permettant :

* La **gestion des utilisateurs**
* La **gestion des livres**
* La **gestion des emprunts**
* L’envoi de **notifications asynchrones via Apache Kafka**

L’application est basée sur une architecture distribuée utilisant **Spring Boot**, **Spring Cloud**, **Docker**, **Eureka**, **API Gateway** et **Kafka**.

Ce projet a été réalisé dans le cadre du module **MSA (Microservices & Systèmes Distribués)**.

---

##  Objectifs du Projet

* Étendre une architecture microservices existante (imposée)
* Implémenter une persistance **MySQL indépendante par service**
* Introduire une communication **asynchrone orientée événements**
* Découpler la logique métier de la logique de notification
* Déployer l’ensemble avec **Docker Compose**
* Respecter les bonnes pratiques des microservices

---

## Architecture Microservices

```
Client
 |
 v
┌──────────────────────────┐
│   API Gateway (9999)     │
└───────────┬──────────────┘
            |
     ┌──────▼───────┐
     │ Eureka Server│ (8761)
     └──────┬───────┘
            |
┌───────────┼─────────────┬──────────────┐
│           │             │              │
▼           ▼             ▼              ▼
User      Book        Emprunt        Notification
Service   Service     Service          Service
(8082)    (8081)      (8085)            (Kafka)
 |         |            |
MySQL     MySQL        MySQL
db_user   db_book     db_emprunt
```

### Détails de l’architecture

* **Eureka Server** assure la découverte dynamique des services
* **API Gateway** est le point d’entrée unique
* Chaque microservice possède **sa propre base de données**
* Kafka permet une **communication asynchrone** entre services

---

##  Technologies Utilisées

* **Java 23**
* **Spring Boot 3.4.1**
* **Spring Cloud 2024**
* **Spring Cloud Gateway**
* **Eureka Server**
* **Spring Data JPA**
* **MySQL 8**
* **Apache Kafka + Zookeeper**
* **Docker & Docker Compose**

---

##  Démarrage du Projet

### 1️⃣ Cloner le projet

```bash
git clone https://github.com/sarataib/ProjetMicroservices.git
cd ProjetMicroservices
```

---

### 2️⃣ Lancer tous les services avec Docker

```bash
docker-compose up -d --build
```

![docker-compose](https://github.com/user-attachments/assets/87d4c2de-c3af-4842-8d6c-f5b4b06446ed)

⏳ Attendre **3 à 4 minutes** pour que tous les services démarrent correctement.

---

### 3️ Vérifier l’état des conteneurs

```bash
docker ps
```

![docker ps](https://github.com/user-attachments/assets/bfd12917-c973-4ec9-83bd-caf3a49ab6fc)

 Tous les services doivent être **UP**.

---

## Bases de Données MySQL

Chaque microservice métier possède **sa propre base de données**, conformément au principe **Database per Service**.

| Service         | Base         |
| --------------- | ------------ |
| User Service    | `db_user`    |
| Book Service    | `db_book`    |
| Emprunt Service | `db_emprunt` |

 Les bases sont **créées automatiquement** via Docker.

###  Vérification manuelle

```bash
docker exec mysql-user mysql -uroot -p123456 -e "SHOW DATABASES;"
docker exec mysql-book mysql -uroot -p123456 -e "SHOW DATABASES;"
docker exec mysql-emprunter mysql -uroot -p123456 -e "SHOW DATABASES;"
```

![mysql](https://github.com/user-attachments/assets/52734004-25fd-4a6e-963c-bcc2bff11312)

---

##  Service Discovery – Eureka

Accéder au dashboard Eureka :

```
http://localhost:8761
```

### Services enregistrés :

* `USER-SERVICE`
* `BOOK-SERVICE`
* `EMPRUNT-SERVICE`
* `GATEWAY-SERVICE`
* `NOTIFICATION-SERVICE`

Eureka permet au Gateway de découvrir dynamiquement les microservices.

---

##  API Gateway

**Port :**

```
http://localhost:9999
```

### Routage configuré

| URL Gateway     | Service         |
| --------------- | --------------- |
| `/api/users/**` | user-service    |
| `/api/books/**` | book-service    |
| `/emprunts/**`  | emprunt-service |

---

##  Kafka – Messagerie Asynchrone

Kafka est utilisé pour **découpler la logique métier de la logique de notification**.

### Topic utilisé

* `emprunt-created`

###  Vérifier les topics Kafka

```bash
docker exec kafka kafka-topics --list --bootstrap-server kafka:9092
```

![kafka topics](https://github.com/user-attachments/assets/8b775f2d-afa5-44c3-8d8e-e6eadcc413f3)

---

##  Tests des APIs

### 1️ Vérifier la santé des services

```bash
curl http://localhost:8082/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8085/actuator/health
curl http://localhost:9999/actuator/health
```

---

### 2️ Scénario de test complet

####  Créer un utilisateur

```bash
curl -X POST http://localhost:9999/api/users \
-H "Content-Type: application/json" \
-d "{\"name\":\"Sara Taibi\",\"email\":\"sara@example.com\"}"
```

---

#### Créer un livre

```bash
curl -X POST http://localhost:9999/api/books \
-H "Content-Type: application/json" \
-d "{\"title\":\"Microservices avec Kafka\",\"author\":\"Spring\",\"available\":true}"
```

---

####  Créer un emprunt (Kafka déclenché)

```bash
curl -X POST http://localhost:9999/emprunts/1/1
```

---

### 3️ Vérifier la réception du message Kafka

```bash
docker exec kafka kafka-console-consumer \
--topic emprunt-created \
--bootstrap-server kafka:9092 \
--from-beginning \
--timeout-ms 5000
```

---

### 4️ Logs du Notification Service

```bash
docker logs -f notification_service
```
<img width="284" height="67" alt="messa" src="https://github.com/user-attachments/assets/5ade5365-0380-4386-8b6c-7cf18cab2196" />


##  Structure du Projet

```
Projet_Microservices/
├── docker-compose.yml          # Configuration Docker complète
├── README.md                   # Ce fichier
├── eureka/                     # Service de découverte
├── gateway/                    # Gateway API
├── user-service/               # Gestion des utilisateurs
│   ├── src/main/resources/application.properties
│   └── Dockerfile
├── book-service/               # Gestion des livres
│   ├── src/main/resources/application.properties
│   └── Dockerfile
├── emprunt-service/            # Gestion des emprunts + Kafka Producer
│   ├── src/main/resources/application.properties
│   └── Dockerfile
└── notification-service/       # Service de notification Kafka Consumer
    ├── src/main/resources/application.properties
    └── Dockerfile
```

---

##  Scénario Fonctionnel Global

1. Le client appelle l’API Gateway
2. Le Gateway route vers le service cible
3. L’emprunt est enregistré en base MySQL
4. Un événement `emprunt-created` est publié dans Kafka
5. Le Notification Service consomme l’événement
6. Une notification est affichée dans les logs

---

## Conclusion

Ce projet démontre :

* Une **architecture microservices robuste**
* Une **découverte dynamique** avec Eureka
* Une **API Gateway centralisée**
* Des **bases de données indépendantes**
* Une **communication asynchrone via Kafka**
* Un **déploiement conteneurisé avec Docker**

