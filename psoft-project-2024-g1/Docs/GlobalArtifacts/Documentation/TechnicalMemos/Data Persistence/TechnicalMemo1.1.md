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
- Adopt the same codebase for different clients that have constraints in the data model and DBMS.

## Alternatives

## Pending Issues
- How difficult and long will be the refactoring of current solution to adopt other persistence data models and DBMS?
- What are the parts of the system that requires modification?
- What data models and DBMS should be supported? (According to the Product Owner, non-relational (document) and relational should be supported. Such as: H2, Oracle, MS SQL, Mongo DB).