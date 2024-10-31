# Technical Memo 1.1

## Problem
The current system requires the ability to persist data across various data models (e.g., relational and document-based) and Database Management Systems (DBMS) such as MySQL, SQL Server, MongoDB, and Redis. Ensuring support for these varied persistence needs within a unified architecture presents challenges in scalability, maintainability, and flexibility.

## Summary of Solution
To meet these requirements, we propose implementing the following strategies to reduce coupling and enhance flexibility:

- **Intermediary Services**: Use intermediary service layers to manage interactions with multiple data providers, isolating business logic from specific DBMS dependencies.
- **Abstracted Services**: Abstract common services, particularly for shared functionalities like authentication and user management, to support multi-model database operations.
- **Deferred Binding**: Implement deferred binding to load configurations dynamically at runtime, allowing flexibility in persistence selection without altering application code.

## Factors
The solution must accommodate multiple client-specific requirements, where clients may have preferences or restrictions on data models and DBMS types. By decoupling the application logic from the persistence layer and enabling runtime configuration.
## Solution Breakdown

1. **Use Intermediary Services**: Intermediaries manage the interaction between application logic and various DBMS providers, isolating any DBMS-specific implementations and promoting flexibility in database choice.
2. **Implement Abstracted Services**: Core services, such as authentication and user management, are abstracted to operate independently of specific DBMS, ensuring these essential functions are universally compatible.
3. **Apply Deferred Binding**: Runtime configuration capabilities defer database binding until the deployment phase, allowing seamless switching between persistence types based on client requirements. Using @Value from Spring Boot is one option.

## Motivation
The proposed solution aims to maintain a single codebase that can support diverse client requirements, especially when those needs involve distinct data models and DBMS. Decoupling the persistence logic provides multiple benefits:

- **Scalability**: The ability to scale and support evolving client requirements for different data models and databases.
- **Maintainability**: Reduced coupling simplifies future updates and minimizes technical debt.
- **Flexibility**: Supporting both relational and non-relational databases expands potential client reach and reduces barriers to adoption.

## Alternatives
- **Direct Coupling to a Specific DBMS**: Tight coupling to a single DBMS (e.g., MySQL) is an alternative but sacrifices flexibility and hinders adaptation when new databases need integration.
- **Separate Codebases for Each DBMS**: Maintaining individual application versions for each supported DBMS could optimize performance but significantly increases development, testing, and maintenance overhead.
- **Database Abstraction Layers (e.g., JPA)**: While frameworks like JPA provide ORM-level abstraction for relational databases, they require extensive adjustments to work with non-relational databases, limiting their effectiveness in a multi-model database context.

## Pending Issues
1. **Refactoring Complexity**: Determining the level of effort required to refactor the current system for multi-model data support requires a thorough impact analysis.
2. **Identifying Modifications**: An assessment is needed to pinpoint areas with tightly coupled persistence logic and to identify required adjustments for compatibility with various data models.
3. **Defining Supported Models and DBMS**: Initial support is planned for both relational and non-relational models, with potential targets including H2, Oracle, SQL Server, and MongoDB as priority DBMS according to product requirements.
