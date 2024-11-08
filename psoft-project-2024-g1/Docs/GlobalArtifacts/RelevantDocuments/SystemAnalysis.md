# Library Management System Analysis Document

_Central City_ library needs a system to manage their library, readers and book lending. The library consists
of thousands of books (no other media formats are available) organized by genre (e.g., Science-fiction,
mistery, Law, Medicine, etc.) that the readers can lend, take home and return after a period (typically 15
days).
In a previous project, the Library Management REST-oriented backend service was
developed.
This application provides REST endpoints for managing:

- Books
- Genres
- Authors
- Readers
- Lendings

## Problem

The Library Management service does not support:

- Extensibility
- Configurability
- Modifiability

## Introduction

The purpose of this document is to provide a detailed architecture design of the new Library Management System by
focusing on three key quality attributes: extensibility, configurability, and modifiability. These attributes were
chosen based on their importance in the design and refactoring of the application.

- **Extensibility:** The system should easily accommodate new features and modules with minimal disruption to existing
  components.

- **Configurability:** The design allow administrators to adjust system parameters and behaviours without requiring
  changes to the source code.

- **Modifiability:** The system should be easily modified to accommodate new requirements or changes in existing
  features.

## Requirements (Drivers)

### Non-Functional Requirements

- Persisting data in different data models (e.g. relational, document) and DBMS (e.g. MySQL, SQL Server, MongoDB,
  Redis);
- Adopting different IAM (Identity and Access Management) providers (e.g. Google, Facebook, Azure);
- Generating Lending and Authors ID in different formats according to varying specifications;
- Recommending Lendings according to varying specifications.

### Constraints

| ID    | Constraint                                                                                                                                                                                               |
|-------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| CON-1 | The actual project is developed in Java, using Spring Boot framework.                                                                                                                                    |
| CON-2 | The system must support relational databases, specifically H2 and document database MongoDB.                                                                                                             |
| CON-3 | The system must support multiple Identity and Access Management (IAM) providers, including Google IAM and Facebook Login.                                                                                |
| CON-4 | The system must support runtime configuration, allowing for the dynamic application of alternative data models, IAM providers, and ID generation formats without requiring code changes or redeployment. |
| CON-5 | The system must be designed to support the implementation of automated functional testing, including unit opaque-box tests, mutation tests, integration tests, and acceptance tests.                     |
| CON-6 | The system should support any frontend.                                                                                                                                                                  |

### Quality Attribute Scenarios (Simplified)

| ID   | Quality Attribute | Scenario                                                                                                                                                                                                                                                                                                                                                                                                                                          | Importance | Risk |
|------|-------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|------|
| QA-1 | Extensibility     | The application is designed to support various data models (relational and document) and multiple databases (MySQL, SQL Server, MongoDB, Redis). It also integrates different Identity and Access Management (IAM) providers for user authentication and generates unique identifiers for Lendings and Authors to ensure the system can be easily extended to accommodate new features, data models, and integrations without significant rework. | H          | H    |
| QA-2 | Configurability   | The data model, IAM provider, lending and author ID formats, as well as the lending recommendation logic, should be dynamically configurable through a configuration file.                                                                                                                                                                                                                                                                        | H          | M    |
| QA-3 | Modifiability     | A new data model/IAM is introduced/removed from the system as part of an update. The DBMS/IAM is added successfully without any changes to the core components of the system.                                                                                                                                                                                                                                                                     | H          | M    |
| QA-4 | Testability       | All functionalities should be tested.                                                                                                                                                                                                                                                                                                                                                                                                             | H          | L    |
| QA-5 | Usability         | The application should be accessible using an HTTP protocol.                                                                                                                                                                                                                                                                                                                                                                                      | H          | M    |

## Quality Attribute Scenarios

### Quality Scenario I - The system is unable to persist in different DBMS

| Element          | Statement                                                                                                                                                                  |
|------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus         | The LMS software does not provide flexibility to persist data across multiple data models.                                                                                 |
| Stimulus source  | A new branch or repository is created every time a new customer constraint arises.                                                                                         |
| Environment      | For now, only one data schema is being used due to DBMS constraints.                                                                                                       |
| Artifact         | Clients and LMS.                                                                                                                                                           |
| Response         | The system must be configured to dynamically support multiple data models and SGBDs without requiring major codebase changes.                                              |
| Response measure | The software must switch between data models and DBMS based on configuration files, without manual intervention, once the configuration is complete and DBMS is available. |

### Quality Scenario II - The system does not allow authentication using different IAMs

| Element          | Statement                                                                                                                                       |
|------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus         | The system does not support multiple Identity and Access Management (IAM) providers.                                                            |
| Stimulus Source  | Only exists an internal authentication method for users, limiting flexibility.                                                                  |
| Environment      | A diverse set of clients uses the service, requiring a flexible, global authentication solution that integrates with widely-used IAM providers. |
| Artifact         | Clients and LMS.                                                                                                                                |
| Response         | The system must allow clients to authenticate using various external IAM providers, configurable via a configuration file.                      |
| Response Measure | The system must support the different authentication methods after configuration changes.                                                       |

### Quality Scenario III - The system only offers a static ID generation method for Lendings and Authors ID

| Element          | Statement                                                                                                                                               |
|------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus         | The system does not support generating Lending and Author IDs in multiple formats based on different requirements or specifications.                    |
| Stimulus Source  | Only a single, fixed format is available for ID generation, limiting flexibility for diverse client needs.                                              |
| Environment      | Various clients require Lending and Author IDs to follow specific formats according to regional, institutional, or regulatory specifications.           |
| Artifact         | Clients and LMS.                                                                                                                                        |
| Response         | The system must generate Lending and Author IDs in various formats based on configuration, allowing flexibility to meet specific client requirements.   |
| Response Measure | The system must begin generating IDs in the required formats immediately after configuration changes, without requiring downtime or code modifications. |

### Quality Scenario IV - The system only offers a static Lending engine

| Element          | Statement                                                                                                                                  |
|------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| Stimulus         | The recommendation engine for Lendings is unable to provide personalized suggestions based on user preferences and varying specifications. |
| Stimulus Source  | Users are receiving generic recommendations that are not filtered.                                                                         |
| Environment      | The system operates in a dynamic environment, each with unique preferences and criteria with lendings.                                     |
| Artifact         | Clients and LMS.                                                                                                                           |
| Response         | The system must filter lendings that reflect individual user specifications and preferences.                                               |
| Response Measure | The system can recommend lendings according to the specifications.                                                                         |

## Iteration 3

### Step 2

#### Goal:

- Support the QA-1 quality attribute scenario (“Application should support various data models.”);
- Support the QA-2 quality attribute scenario (“The data model, IAM provider, lending and author ID formats, as well as
  the lending recommendation logic, should be dynamically configurable through a configuration file.”);
- Support the QA-3 quality attribute scenario (“DBMS/IAM should be added successfully without any changes to the core
  components of the system.”);
- Support the QA-4 quality attribute scenario (“All functionalities should be tested.”);
- Support the CON-2 quality attribute scenario (“The system must support relational databases, specifically H2 and
  document database MongoDB.”);
- Support the CON-3 quality attribute scenario (“The system must support multiple IAM providers, including Google IAM
  and Facebook Login.”);
- Support the CON-4 quality attribute scenario (“The system must support runtime configuration.”);
- Support the CON-5 quality attribute scenario (“The system must be designed to support the implementation of automated
  functional testing, including unit opaque-box tests, mutation tests, integration tests, and acceptance tests.”);

### Step 3

#### Elements to refine:

- Backend project

### Step 4

| Design Decisions and Location | Rationale and Assumptions                                                                                                |
|-------------------------------|--------------------------------------------------------------------------------------------------------------------------|
| Use of property file          | This file is used to create properties of easy access to modify the way the system runs. To support the CON-4, QA-1/2/3. | 

### Step 7

| Not Addressed | Partially Addressed | Completely Addressed | Design Decisions made during the Iteration                                                                     |
|---------------|---------------------|----------------------|----------------------------------------------------------------------------------------------------------------|
|               | QA-1                |                      | No relevant decisions made.                                                                                    |
|               | QA-2                |                      | No relevant decisions made.                                                                                    |
|               |                     | QA-3                 | No relevant decisions made.                                                                                    |
|               | QA-4                |                      | No relevant decisions made.                                                                                    |
|               |                     | QA-5                 | The @Value annotation will be employed to retrieve supported languages from the "application.properties" file. |
|               |                     | CON-1                | No relevant decisions made.                                                                                    |
|               |                     | CON-2                | No relevant decisions made.                                                                                    |
|               |                     | CON-3                | No relevant decisions made.                                                                                    |
|               | CON-4               |                      | No relevant decisions made.                                                                                    |
|               |                     | CON-5                | No relevant decisions made.                                                                                    |
|               |                     | CON-6                | No relevant decisions made.                                                                                    |

## Iteration 4

### Step 2

#### Goal:

- Support the QA-1 quality attribute scenario (“Application should support various data models.”);
- Support the QA-2 quality attribute scenario (“The data model, IAM provider, lending and author ID formats, as well as
  the lending recommendation logic, should be dynamically configurable through a configuration file.”);

### Step 3

Elements to refine:

- Backend project

### Step 4

| Design Decisions and Location | Rationale and Assumptions                                                                                                                                                                                                                                                                                          |
|-------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Use of property file          | TA property file will provide easy access to modify system configurations, enabling support for various data models (QA-1) and dynamic configurations (QA-2). Properties will be loaded at application startup using Spring's @Value annotation to ensure they are available throughout the application lifecycle. |


### Step 7

| Not Addressed | Partially Addressed | Completely Addressed | Design Decisions made during the Iteration                                                         |
|---------------|---------------------|----------------------|----------------------------------------------------------------------------------------------------|
|               |                     | QA-1                 | Use a property file to configure the way the system runs, allowing for flexibility in data models. |
|               |                     | QA-2                 | Use a property file to configure the data model, IAM provider, and ID formats dynamically.         |
|               |                     | QA-3                 | No relevant decisions made.                                                                        |
|               | QA-4                |                      | No relevant decisions made.                                                                        |
|               |                     | QA-5                 | No relevant decisions made.                                                                        |
|               |                     | CON-1                | No relevant decisions made.                                                                        |
|               |                     | CON-2                | No relevant decisions made.                                                                        |
|               |                     | CON-3                | No relevant decisions made.                                                                        |
|               | CON-4               |                      | No relevant decisions made.                                                                        |
|               |                     | CON-5                | No relevant decisions made.                                                                        |
|               |                     | CON-6                | No relevant decisions made.                                                                        |

## Iteration 5

### Step 2

#### Goal:

- Support the QA-4 quality attribute scenario (“All functionalities should be tested.”);
- Support the CON-4 quality attribute scenario (“The system must support runtime configuration.”);

### Step 3

Elements to refine:

- Backend project

### Step 4

| Design Decisions and Location                        | Rationale and Assumptions                                                                                                                                                                                                                                                                                                                              |
|------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Use Unit, Integration, Acceptance and Mutation tests | These tests will ensure that all functionalities are thoroughly tested and meet quality requirements (QA-4). Unit tests will cover individual components, integration tests will verify the interactions between components, acceptance tests will validate the overall functionality, and mutation tests will help ensure robustness against changes. |
| Runtime configuration                                | Implementing runtime configuration will allow dynamic updates to system settings without requiring a restart, thus improving flexibility and responsiveness to configuration changes (CON-4). This can be achieved using Spring Cloud Config or a similar mechanism.                                                                                   |

### Step 7

| Not Addressed | Partially Addressed | Completely Addressed | Design Decisions made during the Iteration                                                                  |
|---------------|---------------------|----------------------|-------------------------------------------------------------------------------------------------------------|
|               |                     | QA-1                 | No relevant decisions made.                                                                                 |
|               |                     | QA-2                 | No relevant decisions made.                                                                                 |
|               |                     | QA-3                 | No relevant decisions made.                                                                                 |
|               |                     | QA-4                 | Use Unit, Integration, Acceptance, and Mutation tests to ensure all functionalities are tested effectively. |
|               |                     | QA-5                 | No relevant decisions made.                                                                                 |
|               |                     | CON-1                | No relevant decisions made.                                                                                 |
|               |                     | CON-2                | No relevant decisions made.                                                                                 |
|               |                     | CON-3                | No relevant decisions made.                                                                                 |
|               |                     | CON-4                | Implement runtime configuration to allow dynamic updates to settings.                                       |
|               |                     | CON-5                | No relevant decisions made.                                                                                 |


# Next

[System Architeture Design](SystemArchitetureDesign.md)

# References

1. P. Clements, D. Garlan, R. Little, R. Nord, and J. Stafford, Software Architecture Documentation. [Online].
   Available: https://www.se.rit.edu/~co-operators/SoftwareArchitectureDocumentation.pdf. [Accessed: 31-Oct-2024].
2. P. Lago and H. V. Vliet, "Quality Attributes in Software Architecture Design," Citeseer, [Online].
   Available: https://citeseerx.ist.psu.edu/document?repid=rep1&type=pdf&doi=53a5bc28c103f360cfc189a0bd3c5b2d52a00f49. [Accessed: 31-Oct-2024].
3. W. Wood, "A Practical Example of Applying Attribute-Driven Design (ADD), Version 2.0," Carnegie Mellon University,
   Software Engineering Institute's Digital Library. Software Engineering Institute, Technical Report
   CMU/SEI-2007-TR-005, 1-Feb-2007 [Online]. Available: https://doi.org/10.1184/R1/6571706.v1. [Accessed: 31-Oct-2024].