# Library Management System Architecture Analysis Document

## Introduction

The purpose of this document is to provide a detailed architecture design of the new Library Management System by focusing on three key quality attributes: extensibility, configurability, and modifiability. These attributes were chosen based on their importance in the design and refactoring of the application.
 - **Extensibility:** The system should easily accommodate new features and modules with minimal disruption to existing components.
 - **Configurability:** The design allow administrators to adjust system parameters and behaviours without requiring changes to the source code.
 - **Modifiability:** The system should be easily modified to accommodate new requirements or changes in existing features.

## Requirements

### Non-Functional Requirements
- Persisting data in different data models (e.g. relational, document) and DBMS (e.g. MySQL, SQL Server, MongoDB, Redis);
- Adopting different IAM (Identity and Access Management) providers (e.g. Google, Facebook, Azure);
- Generating Lending and Authors ID in different formats according to varying specifications;
- Recommending Lendings according to varying specifications.

## Constraints

| ID    | Constraint                                                                                                                                                                                               |
|-------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| CON-1 | The actual project is developed in Java, using Spring Boot framework.                                                                                                                                    |
| CON-2 | The system must support relational databases, specifically H2, Oracle, and MS SQL Server, as well as document database MongoDB.                                                                          |
| CON-3 | The system must support multiple Identity and Access Management (IAM) providers, including Google IAM, Azure AD, and Facebook Login.                                                                     |
| CON-4 | The system must support runtime configuration, allowing for the dynamic application of alternative data models, IAM providers, and ID generation formats without requiring code changes or redeployment. |
| CON-5 | The system must be designed to support the implementation of automated functional testing, including unit opaque-box tests, mutation tests, integration tests, and acceptance tests.                     |
| CON-6 | The system should support any frontend.                                                                                                                                                                  |

## Quality Attribute Scenarios (Simplified)

| ID   | Quality Attribute | Scenario                                                                                                                                                                                                                                                                                                                                                                                                                                          | Priority |
|------|-------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|
| QA-1 | Extensibility     | The application is designed to support various data models (relational and document) and multiple databases (MySQL, SQL Server, MongoDB, Redis). It also integrates different Identity and Access Management (IAM) providers for user authentication and generates unique identifiers for Lendings and Authors to ensure the system can be easily extended to accommodate new features, data models, and integrations without significant rework. |          |
| QA-2 | Configurability   | The data model, IAM provider, lending and author ID formats, as well as the lending recommendation logic, should be dynamically configurable through a configuration file.                                                                                                                                                                                                                                                                        |          |
| QA-3 | Modifiability     | A new data model/IAM is introduced/removed from the system as part of an update. The DBMS/IAM is added successfully without any changes to the core components of the system.                                                                                                                                                                                                                                                                     |          |

## Quality Attribute Scenarios

### Quality Scenario I - The system is unable to persist in different DBMS

| Element           | Statement                                                                                                                                                                  |
|-------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus          | The LMS software does not provide flexibility to persist data across multiple data models.                                                                                 |
| Stimulus source   | A new branch or repository is created every time a new customer constraint arises.                                                                                         |
| Environment       | For now, only one data schema is being used due to DBMS constraints.                                                                                                       |
| Artifact          | Clients and LMS.                                                                                                                                                           |
| Response          | The system must be configured to dynamically support multiple data models and SGBDs without requiring major codebase changes.                                              |
| Response measure  | The software must switch between data models and DBMS based on configuration files, without manual intervention, once the configuration is complete and DBMS is available. |
| Quality attribute | Configurability, Extensibility and Modifiability.                                                                                                                          |

### Quality Scenario II - The system does not allow authentication using different IAMs

| Element           | Statement                                                                                                                                       |
|-------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus          | The system does not support multiple Identity and Access Management (IAM) providers.                                                            |
| Stimulus Source   | Only exists an internal authentication method for users, limiting flexibility.                                                                  |
| Environment       | A diverse set of clients uses the service, requiring a flexible, global authentication solution that integrates with widely-used IAM providers. |
| Artifact          | Clients and LMS.                                                                                                                                |
| Response          | The system must allow clients to authenticate using various external IAM providers, configurable via a configuration file.                      |
| Response Measure  | The system must support the different authentication methods after configuration changes.                                                       |
| Quality attribute | Configurability and Extensibility.                                                                                                              |

### Quality Scenario III - The system only offers a static ID generation method for Lendings and Authors ID

| Element           | Statement                                                                                                                                               |
|-------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus          | The system does not support generating Lending and Author IDs in multiple formats based on different requirements or specifications.                    |
| Stimulus Source   | Only a single, fixed format is available for ID generation, limiting flexibility for diverse client needs.                                              |
| Environment       | Various clients require Lending and Author IDs to follow specific formats according to regional, institutional, or regulatory specifications.           |
| Artifact          | Clients and LMS.                                                                                                                                        |
| Response          | The system must generate Lending and Author IDs in various formats based on configuration, allowing flexibility to meet specific client requirements.   |
| Response Measure  | The system must begin generating IDs in the required formats immediately after configuration changes, without requiring downtime or code modifications. |
| Quality attribute | Configurability and Extensibility.                                                                                                                      |

### Quality Scenario IV - The system only offers a static Lending engine

| Element           | Statement                                                                                                                                  |
|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus          | The recommendation engine for Lendings is unable to provide personalized suggestions based on user preferences and varying specifications. |
| Stimulus Source   | Users are receiving generic recommendations that are not filtered.                                                                         |
| Environment       | The system operates in a dynamic environment, each with unique preferences and criteria with lendings.                                     |
| Artifact          | Clients and LMS.                                                                                                                           |
| Response          | The system must filter lendings that reflect individual user specifications and preferences.                                               |
| Response Measure  | The system can recommend lendings according to the specifications.                                                                         |
| Quality attribute | Configurability.                                                                                                                           |

## References
[Software Architecture Documentation](https://www.se.rit.edu/~co-operators/SoftwareArchitectureDocumentation.pdf)

[Quality Attributes in Software Architecture Design](https://citeseerx.ist.psu.edu/document?repid=rep1&type=pdf&doi=53a5bc28c103f360cfc189a0bd3c5b2d52a00f49)
