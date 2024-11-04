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
With the new requirements, the diagram shows that system will be composed by a backend and an external LMS API and Auth API.

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

This design shows the implementation and the logical view of the system. System will implement a API Gateway, which will be responsible for the communication between the backend and the external services.

This diagram shows the management of entities, which are responsible for the business logic and data access.

## Logical View Level 3

![LogicalViewN3.svg](/Docs/Diagrams/LogicalViewN3.svg)

The Logical View Level 3 diagram shows the proposed architecture of the system. The system will be composed by a backend, an external LMS API, an external Database API and an external Auth API.

## Sequence Diagram Level 3

![SDCreateReaderN3.svg](/Docs/Diagrams/SDCreateReaderN3.svg)

This diagram shows the sequence of events that occur when a user creates a reader. The user sends a request to the backend, which then sends a request to the external LMS API to create the reader. The external LMS API then sends a response back to the backend, which sends a response back to the user.

![SDPersonalDataN3.svg](/Docs/Diagrams/SDPersonalDataN3.sgv)

This diagram shows the sequence of events that occur when a user requests personal data. The user sends a request to the backend, which then sends a request to the external Auth API to get the user's personal data. The external Auth API then sends a response back to the backend, which sends a response back to the user.

## Implementation Logical View Level 4

![ImplementationLogicalN4.svg](/Docs/Diagrams/ImplementationLogicalN4.svg)

This diagram shows the implementation and the logical view of the system. System will implement a API Gateway, which will be responsible for the communication between the backend and the external services.

This diagram shows the management of entities, which are responsible for the business logic and data access.

## Conclusion 

This document has presented the system architecture design of the project. It has provided a high-level overview of the system, its components, and the interactions between them. The system architecture design is an important part of the software development process, as it helps to ensure that the system is well-structured, scalable, and maintainable. The proposed architecture of the system includes a backend, an external LMS API, an external Database API, and an external Auth API. The system will be deployed with a backend, with an external client requesting the services and IAM with HTTP/HTTPS requests. The system will implement a API Gateway, which will be responsible for the communication between the backend and the external services. The system will also implement entities, which are responsible for the business logic and data access. The sequence of events that occur when a user creates a reader and requests personal data have been presented in sequence diagrams. The proposed architecture of the system is well-structured, scalable, and maintainable, and will help to ensure the success of the project.






