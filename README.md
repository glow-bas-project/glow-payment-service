# GLOW Payment Service

Backend microservice responsible for payment-related functionality within the GLOW platform. It is part of a distributed system consisting of multiple independently deployable microservices.

Built with **Java** and **Quarkus**, the service uses **PostgreSQL** for persistence and **Liquibase** for database migrations. It communicates with a dedicated Mock Stripe API microservice to simulate payment requests and responses without interacting with the real Stripe platform.

Technologies:
* Java 25 & Quarkus
* PostgreSQL & Liquibase
* OpenID Connect (OIDC)
* Kubernetes
* Mock Stripe API
* Gradle
