# System Architecture Design

## 1. Introduction

This document presents the system architecture design of the project. It is intended to provide a high-level overview of the system, its components, and the interactions between them. 
The system architecture design is an important part of the software development process, as it helps to ensure that the system is well-structured, scalable, and maintainable.

## 2. System Overview
 
System architecture was changed because of new requirements, as a result, the diagrams can be visualized in two different ways: 
System As Is and System To Be. The System As Is diagram shows the current architecture of the system, while the System To Be diagram shows the proposed architecture of the system.

### 2.1 Logical View Level 1

![LogicalViewN1.svg](/Docs/Diagrams/LogicalViewN1.svg)

As we can see in the diagram, system initial architecture is composed just for the backend, which is responsible for the business logic and data access.
With the new requirements, the diagram shows that system will be composed by a backend, an external LMS API and Auth API.

### 2.2 Implementation View Level 1

![ImplementationViewN1.svg](/Docs/Diagrams/ImplementationViewN1.svg)

The Implementation View Level 1 diagram shows the current implementation of the system. The system is implemented with backend with various IAM services and LMS Rest API.

### 2.3 Logical View Level 2

![LogicalViewN2.svg](/Docs/Diagrams/LogicalViewN2.svg)

The Logical View Level 2 diagram shows the proposed architecture of the system. The system will be composed by a backend, an external LMS API and an external Database API.

### 2.4 Physical View Level 2

![PhysicalViewN2.svg](/Docs/Diagrams/PhysicalViewN2.svg)

The Physical View Level 2 diagram shows the proposed physical architecture of the system. The system will be deployed with a backend, with an external client requesting the services and IAM with HTTP/HTTPS requests.

### Implementation View Level 2

![ImplementationViewN2.svg](/Docs/Diagrams/ImplementationViewN2.svg)

The Implementation View Level 2 diagram shows the proposed implementation of the system. System will implement a backend with Database Management System in LMS.

## Implementation Logical View Level 3

![ImplementationLogicalN3.svg](/Docs/Diagrams/ImplementationLogicalN3.svg)

In LMS, the API is the entry point for the system, exposing endpoints for resources like authors, books, readers and lendings.
Service is the business logic layer, that processes API requests, coordinating operations and interacting with repositories and data models.
Repository is responsible for data access and persistence, providing methods to interact with the data layer.
Model represents the system's entities (Author, Book, Reader and Lending).
The API and Services are connected, which manage the business logic.
Services depend on Repositories for data operations.
Repositories interact with the Model for data manipulation.

## Logical View Level 3

![LogicalViewN3.svg](/Docs/Diagrams/LogicalViewN3.svg)

### System As Is

The current architecture has a layered approach with four main layers:
- Frameworks and Drivers: responsible for the communication between the system and the external services, the router is responsible for directing requests to the appropriate controllers. The Repository Drivers acts as an interface for data access.
- Interface Adapter Layer: The Controller interacts with the Router, handling income API requests. The Repository interacts with the Repository Driver, managing data access and persistence.
- Application Business Rules: Responsible for managing business logic related to applications and interacting with the Controller and Model Layers.
- Enterprise Business Rules: Contains the Model component, which represents the core entities and logic of the system.

### System To Be

The target architecture introduces additional components and layers to support more advanced functionalities, such as authentication.
- Frameworks and Drivers Layer: The AuthN Driver is an addition to handle interactions with an external authentication provider (AuthN API), indicating plans for improved or externalized authentication.
- Interface Adapter Layer: The AuthN Adapter works with the AuthN Driver to manage authentication tasks. This layer now has expanded responsibilities, managing both data and authentication access.
- Application Business Rules: Contains the App Service component, which has not changed significantly but now has additional connections to handle different types of business rules.
- Enterprise Business Rules: Domain Service is a new addition, indicating a separation of domain-specific business rules from the general model, possibly to handle more complex logic independently of the data model.

## Sequence Diagram Level 3

![SDCreateReaderN3.svg](/Docs/Diagrams/SDCreateReaderN3.svg)

This diagram shows the sequence of events that occur when a user creates a reader. The user sends a request to the backend, which then sends a request to the external LMS API to create the reader. The external LMS API then sends a response back to the backend, which sends a response back to the user.

![SDPersonalDataN3.svg](/Docs/Diagrams/SDPersonalDataN3.svg)

This diagram shows the sequence of events that occur when a user requests personal data. The user sends a request to the backend, which then sends a request to the external Auth API to get the user's personal data. The external Auth API then sends a response back to the backend, which sends a response back to the user.

## Implementation Logical View Level 4

![ImplementationLogicalN4.svg](/Docs/Diagrams/ImplementationLogicalN4.svg)

This diagram shows the implementation and the logical view of the system. System will implement an API Gateway, which will be responsible for the communication between the backend and the external services. 

The Backend shows the components that from the backend system, specifically designed to manage REST API interactions with the LMS. It follows a Clean Architecture style, separating different layers of responsibility.

- Router: Routes external requests coming from the LMS REST API to the appropriate controllers in backend.
- Controller: The Controller Layer receives requests from the Router and interacts with the App Service, handling business logic and orchestrating responses.
- App Service: Represents the application's business logic layer. The App Service interacts with the Model and Repository layers, managing application rules.
- Repository: Manages data access and persistence, abstracting interactions with the Driver component (which interfaces with the database). Connects to the Driver API to communicate with the database.
- Model: Contains the core entities and data structure of the system. Provides the foundation structure that the application business rules operate on.

The Management System is and abstraction of different modules that the system manages, such as Authors, Books, Readers and Lendings. It uses components that reflect a service-based architecture with different layers.

- API: Acts as the entry point for the Management system, receiving requests and directing them to the relevant services. 
- Services: Handles the business logic within the Management system. Communicates with Infrastructure.Repositories.Impl and the Model to perform various operations.
- Infrastructure.Repositories.Impl: Provides the concrete implementation of the repositories. Interacts with the Repositories layer for data persistence.
- Repositories: Manages data access within the Management system, similar to the Repository in the LMS REST API.
- Model: Similar to the LMS REST API Model, it defines the data structure and entities that the Management system operates on.

Connection between LMS REST API and Management System

The dotted arrows labeled "<<manifest>>" represent dependencies or relationships between corresponding layers or components in the two systems. This shows how the backend API components are mapped or related to equivalent components in the Management system:

- The Router in LMS REST API connects to the API in the Management system.
- The Controller in LMS REST API connects to Services in the Management system.
- Repository connects to Infrastructure.Repositories.Impl and Repositories.
- The Model in both systems serves as the foundation of the data structure, and they relate to each other for managing similar data entities.

## Conclusion 

This document has presented the system architecture design of the project. It has provided a high-level overview of the system, its components, and the interactions between them. The system architecture design is an important part of the software development process, 
as it helps to ensure that the system is well-structured, scalable, and maintainable. The proposed architecture of the system includes a backend, an external LMS API, an external Database API, and an external Auth API. The system will be deployed with a backend, 
with an external client requesting the services and IAM with HTTP/HTTPS requests. The system will implement a API Gateway, which will be responsible for the communication between the backend and the external services. The system will also implement entities, 
which are responsible for the business logic and data access. The sequence of events that occur when a user creates a reader and requests personal data have been presented in sequence diagrams. The proposed architecture of the system is well-structured, scalable, 
and maintainable, and will help to ensure the success of the project.

