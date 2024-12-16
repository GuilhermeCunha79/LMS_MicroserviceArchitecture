# Technical Memo 1.1 - Microservices Separation Strategy

## Problem
The current system lacks clear separation between different business domains, which can lead to tight coupling, scalability issues, and difficulty in maintaining or evolving different parts of the system independently. The absence of well-defined boundaries between services increases complexity, making it harder to scale, deploy, and modify individual components without impacting others.

## Summary of Solution
To address these challenges, we propose a microservices architecture based on Domain-Driven Design (DDD) principles, which will isolate distinct business domains into independent, self-contained services. This separation will allow for better maintainability, scalability, and flexibility across the system. The key strategies include:

- **Domain Cohesion**: Group related entities and business logic into dedicated services that reflect clear business capabilities (e.g., Books, Users, Lending).
- **Loose Coupling**: Ensure that services communicate through well-defined APIs and avoid direct dependencies, allowing for easier maintenance and independent evolution.
- **Independent Deployment and Scaling**: Allow each service to be deployed, updated, and scaled independently to better manage the load and ensure service continuity.
- **Inter-Service Communication**: Use asynchronous messaging or REST APIs for communication between services to decouple them while still enabling necessary collaboration.
- **Strangler Fig Pattern**: Gradually migrate from the monolithic system to the microservices architecture using the Strangler Fig Pattern. This pattern ensures a smooth transition by replacing monolithic components incrementally with microservices, allowing for continuous functionality throughout the migration process.

## Factors
The solution needs to accommodate varying business requirements and scale according to demand. As each microservice is independent, its configuration, deployment, and scaling can be customized to meet the specific needs of each domain, ensuring a more responsive system. Additionally, isolating the domains reduces the risk of errors and makes it easier to enforce clear separation of concerns.

## Solution Breakdown

1. **Domain Cohesion**: Based on DDD principles, the system is divided into clear bounded contexts:
   - **Books Context**: Manages book-related entities (Book, Author, Genre). This grouping follows the principles of **Domain-Driven Design (DDD)**, ensuring that all related operations and data management for books are handled within the same context. By keeping **Author** and **Genre** within the **Books Context**, we maintain **domain coherence**, ensuring that operations like book creation, genre classification, and author information are tightly coupled and encapsulated.
   - **Users Context**: Responsible for user management, including authentication and authorization for different roles (e.g., reader, librarian).
   - **Lendings Context**: Handles book borrowing, tracking due dates, returns, and overdue penalties.
   - **Recommendation Context**: Provides personalized book recommendations based on reader preferences.
   - **Reader Context**: Manages reader-specific data.

2. **Loose Coupling**: Each microservice is designed to interact with other services through APIs or event-driven communication mechanisms, ensuring they are decoupled and can evolve independently. For instance, the Lending microservice interacts with the Book and Reader services but does not directly depend on them.

3. **Independent Deployment and Scaling**: Each microservice can be deployed and scaled independently. For example, the Recommendation service may require more compute power during peak usage, while the Lending service may not.

4. **Inter-Service Communication**: Communication between services will be achieved through well-defined APIs, ensuring that each service can provide and consume necessary data while maintaining autonomy.

## Motivation
The proposed microservices separation ensures that the system can handle growing complexity and future business requirements. By following DDD principles and decoupling the services, we provide:

- **Scalability**: Each service can be scaled based on its own load requirements, supporting the growing demands of specific domains without overloading the system.
- **Maintainability**: With clear boundaries between services, changes to one domain (e.g., adding a new genre type in the Books service) will not affect others, reducing the risk of bugs and improving overall maintainability.
- **Flexibility**: The separation allows each service to be updated, replaced, or extended independently. New business domains can be added with minimal disruption to existing functionality.

## Alternatives
- **Monolithic Architecture**: A monolithic approach would keep all logic within a single service, making it harder to scale, deploy, and maintain individual components. This could lead to performance bottlenecks and difficulties in managing different business domains.
- **Single Service per DBMS**: Rather than separating business domains, one could isolate services based on database types (e.g., one service for relational DBMS and another for NoSQL). This would complicate the system without addressing the underlying need for domain separation.
- **Tightly Coupled Services**: Tight coupling between services could lead to shared databases or direct dependencies, which would hinder the ability to scale or update individual components independently.

## Pending Issues
1. **Impact Analysis**: A detailed analysis of how existing functionalities will be impacted by the separation of microservices is needed. This includes reviewing the interaction patterns between services and potential changes required for API development.
2. **Service Boundaries Refinement**: Additional work is required to refine the boundaries of each service to ensure they align with the business needs while avoiding unnecessary overlap or fragmentation.