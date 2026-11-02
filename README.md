# Quali-Track

Application de **suivi de production en atelier**, construite en architecture microservices (Java 17, Spring Boot, Spring Cloud, Maven) dans le cadre du PFM de 5ème année (EMSI, cours J2EE avancé et Spring Boot).

Un **lot de production** est lancé sur une machine avec des matières premières, puis soumis à un contrôle qualité :

- **validé** : les produits conformes entrent en stock et la machine est libérée ;
- **rebuté** : les défauts sont enregistrés, et la machine est libérée ou placée en maintenance selon la cause du rejet ;
- **annulé** : si le lot n'a jamais démarré (ressources indisponibles), la machine et les matières réservées sont restituées.

Cycle de vie d'un lot : `EN_ATTENTE_RESSOURCES → EN_PRODUCTION → EN_CONTROLE → VALIDE / REBUTE`, ou `EN_ATTENTE_RESSOURCES → ANNULE`.

## Diagramme de classes

Chaque service possède ses propres entités. Les flèches en pointillés représentent les références entre services (par identifiant), sans préjuger du protocole de communication.

```mermaid
classDiagram
    direction LR

    namespace service_atelier {
        class Machine {
            id : Long
            nom : String
            type : String
            cadenceHeure : double
            statut : StatutMachine
        }
        class StatutMachine {
            <<enumeration>>
            DISPONIBLE
            RESERVEE
            EN_SERVICE
            EN_MAINTENANCE
        }
        class RawMaterial {
            id : Long
            nom : String
            fournisseur : String
            stock : double
            unite : String
        }
    }

    namespace service_production {
        class ProductionLot {
            id : Long
            machineId : Long
            produit : String
            quantiteProjetee : int
            quantiteProduite : int
            dateDebut : Date
            statut : StatutLot
        }
        class StatutLot {
            <<enumeration>>
            EN_ATTENTE_RESSOURCES
            EN_PRODUCTION
            EN_CONTROLE
            VALIDE
            REBUTE
            ANNULE
        }
        class QualityCheck {
            id : Long
            lotId : Long
            controleurId : Long
            date : Date
            tauxDefectueux : double
            conforme : boolean
        }
        class ProductionMaterial {
            id : Long
            lotId : Long
            rawMaterialId : Long
            quantiteNecessaire : double
        }
    }

    namespace service_stock {
        class FinishedGood {
            id : Long
            lotId : Long
            reference : String
            quantite : int
            entrepot : String
            statut : String
        }
        class Defect {
            id : Long
            lotId : Long
            type : String
            quantite : int
            cause : String
        }
    }

    namespace service_utilisateurs {
        class User {
            id : Long
            nom : String
            email : String
            role : String
            actif : boolean
        }
    }

    %% Relations internes
    Machine --> StatutMachine : statut
    ProductionLot --> StatutLot : statut
    ProductionLot "1" *-- "0..*" ProductionMaterial : besoins
    ProductionLot "1" *-- "0..*" QualityCheck : contrôles

    %% Dépendances entre services (références par identifiant)
    ProductionLot ..> Machine : référence par machineId
    ProductionMaterial ..> RawMaterial : référence par rawMaterialId
    QualityCheck ..> User : référence par controleurId
    FinishedGood ..> ProductionLot : référence par lotId
    Defect ..> ProductionLot : référence par lotId
```

## Architecture

| Application | Rôle | Port |
|---|---|---|
| `api-gateway` | Point d'entrée unique, routage vers les services | 8080 |
| `eureka-server` | Service Registry (découverte des services) | 8761 |
| `service-atelier` | Machines et matières premières | 8081 |
| `service-production` | Lots de production, contrôles qualité (service principal) | 8082 |
| `service-stock` | Produits finis et défauts | 8083 |
| `service-utilisateurs` | Utilisateurs (contrôleurs qualité) | 8084 |
| `service-notification` | Notifications au responsable de production *(à venir, jalon V5)* | 8085 |

```mermaid
flowchart LR
    client([Client / Frontend]) <--> gw

    subgraph infra [Infrastructure]
        gw["API Gateway<br/>:8080"]
        eureka["Eureka Server<br/>:8761"]
    end

    subgraph metier [Services métier]
        atelier["service-atelier<br/>:8081"]
        production["service-production<br/>:8082"]
        stock["service-stock<br/>:8083"]
        utilisateurs["service-utilisateurs<br/>:8084"]
    end

    notification["service-notification<br/>:8085<br/>(à venir, V5)"]

    gw --> atelier
    gw --> production
    gw --> stock
    gw --> utilisateurs

    gw -.-> eureka
    atelier -.-> eureka
    production -.-> eureka
    stock -.-> eureka
    utilisateurs -.-> eureka

    style notification stroke-dasharray: 5 5
```

Flèches pleines : routage des requêtes via la Gateway. Flèches en pointillés : enregistrement et découverte via Eureka.
