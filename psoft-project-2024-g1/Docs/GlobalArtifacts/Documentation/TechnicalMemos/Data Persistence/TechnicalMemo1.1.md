# Technical Memo 1.1

## Problem:
**Persisting data in different data models (e.g. relational, document) and SGBD (e.g.MySQL, SQL Server, MongoDB, Redis).

## Summary of Solutions:
Adopt the following tactics:
- **Reduce coupling** namely:
    - Use intermediary services to manage multiple providers;
    - Abstract common services for authentication and user management;
    - Defer binding to address configuration.

## Factors

## Solution:
1. **Use intermediary:**

2. **Use abstract common services:**

3. **Defer binding:**
    
## Motivation
- The motivation behind this solution is to ensure that the same codebase can support different client needs, particularly when 
clients have constraints regarding the data model and DBMS. By decoupling the application logic from the persistence layer and allowing for runtime configuration, we can achieve:

  - Scalability: Ability to support multiple data models and databases as client needs evolve.
  - Maintainability: Reduced coupling leads to easier maintenance and updates.
  - Flexibility: Supporting both relational and non-relational databases ensures that we can cater to a wide range of client requirements without major code changes.

## Alternatives
- Direct Coupling: One alternative would be to tightly couple the application with a specific DBMS (e.g., MySQL). However, this approach reduces flexibility and makes the system harder to adapt when a new database is introduced.
- Separate Codebases for Each DBMS: Another approach could be to maintain separate versions of the application for each supported DBMS. While this provides maximum optimization for each DBMS, it significantly increases development and maintenance efforts.
- Database Abstraction Layers (e.g., JPA): Using frameworks like JPA for relational databases provides abstraction at the ORM level. However, it may not be suitable for non-relational databases without significant adjustments.

## Pending Issues
- Complexity of Refactoring: How difficult and time-consuming will it be to refactor the current solution to support multiple persistence data models and DBMS? A detailed impact assessment is needed.
- Identifying Modifications: Which parts of the system will require modification to accommodate new persistence strategies? This includes identifying areas where persistence logic is tightly coupled.
- Supported Data Models and DBMS: Which data models and DBMS should be supported initially? According to the Product Owner, both non-relational (document) and relational models should be supported, including systems like H2, Oracle, MS SQL, and MongoDB.