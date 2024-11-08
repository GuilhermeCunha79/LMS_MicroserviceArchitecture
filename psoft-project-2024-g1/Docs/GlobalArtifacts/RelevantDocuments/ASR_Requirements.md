# Architecturally Significant Requirements (ASRs)

**ASRs** represent a subset of non-functional requirements that directly influence the architecture and overall design of a system. These requirements do not focus on specific functionalities but instead address fundamental system attributes necessary for ensuring efficiency, maintainability, and scalability over time. For the library management system, the following ASRs are outlined:

## 1. Performance
The system must exhibit optimal speed and efficiency. For instance, the **CI/CD pipeline** must complete its entire process, including build, test, and deployment phases, in under **10 minutes**. This ensures quick feedback and a seamless workflow, which is critical for maintaining a high level of development velocity and operational efficiency.

## 2. Reliability
The system must be resilient and dependable. A key **reliability target** is maintaining over **80% test coverage** (in future iterations), minimizing the likelihood of defects being introduced into production. To further enhance system reliability, **static analysis** tools such as **SonarQube** are integrated to identify and address potential code issues like code smells, unnecessary complexity, and vulnerabilities, ensuring that the codebase remains robust and stable over time.

## 3. Scalability
The system must be designed to accommodate growth in both **user volume** and **data** without a degradation in performance. It should be highly **configurable** and flexible enough to handle incremental changes with minimal code modification. The architecture should be adaptable to support the integration of new modules or larger datasets, thereby promoting long-term scalability without extensive rework.

## 4. Maintainability
The system's codebase must be structured to facilitate easy maintenance and future upgrades. **Static code analysis** tools like **CheckStyle** are employed to ensure that the code adheres to industry-standard **styling guidelines**, improving readability and comprehension for developers. This includes a focus on **modularity**, which allows for targeted updates and changes with minimal disruption to other parts of the system, thus ensuring smooth and cost-effective maintenance over time.

## 5. Modifiability
The system must be designed to be easily modifiable, allowing the introduce of changes or enhancements with minimal effort and risk. The architecture should support a clear separation of concerns, where different components of the system can be modified independently. Using well-defined interfaces and design patterns, the system must accommodate evolving requirements without significant architectural changes, thereby making it easier to adapt to future needs and reduce the cost of modifications.

# Documenting and Validating ASRs

To ensure that the ASRs are consistently met throughout the development lifecycle, the system integrates automated validation tools into the **CI/CD pipeline**. These tools include:

- **Test Coverage Monitoring:** Tools like **JaCoCo** are used to generate coverage reports, verifying that critical components of the codebase are thoroughly tested to meet the reliability and performance standards.

- **Code Quality Analysis:** **SonarQube** is configured to continuously assess the code for **complexity**, **code duplication**, and **vulnerabilities**, producing detailed reports to monitor code quality and highlight areas for improvement. These metrics ensure that the system maintains high standards of quality and robustness at all stages of development.

By embedding these mechanisms into the workflow, the system ensures that the **ASRs** are continually validated, allowing for consistent performance, reliability, scalability, and maintainability in line with best practices and industry standards.
