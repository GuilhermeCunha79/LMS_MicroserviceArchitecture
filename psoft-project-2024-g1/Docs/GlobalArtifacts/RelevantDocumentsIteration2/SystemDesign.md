# System Architecture Design

## 1. Introduction

This document presents the system architecture design, offering a comprehensive overview of the project's structure, key components,
and interaction patterns. The architecture is crafted to ensure Extensibility, Configurability, and Modifiability, aligning 
with current project requirements and anticipated future needs.

## 2. System Overview

In response to evolving requirements, the system architecture has been restructured and is presented in two distinct views: 
"System As Is" and "System To Be." The "As Is" view represents the original architecture, while the "To Be" 
view illustrates the proposed future-state architecture.

### Domain Model
![DomainModel.svg](DomainModel.svg)

### 2.1 Level 1

#### 2.1.1 Logical View

![LogicalViewN1.svg](./Diagrams/NV1/VI-NV1-Phase 2.svg)

### 2.2 Level 2

#### 2.2.1 Logical View

![LogicalViewN2.svg](./Diagrams/NV2/VL-N2-Phase2.svg)

#### 2.2.2 Physical View

![PhysicalViewN2.svg](./Diagrams/NV2/VF-N2-Phase2.svg)

#### 2.2.3 Implementation View

![ImplementationViewN2.svg](./Diagrams/NV2/VI-N2-Phase2.svg)

#### 2.2.3 Process View - UC3

![ProcessViewN2.svg](./Diagrams/NV2/VP-NV2-UC3-Phase2.svg)

### Level 3

#### 2.3.1 Logical View

![LogicalViewN3.svg](./Diagrams/NV3/VL-NV3-Phase 2.svg)

#### 2.3.2 Implementation View

![ImplementationViewN3.svg](./Diagrams/NV3/VI-NV3-Phase2.svg)

##### 2.3.2.1 Implementation View NV4 vs Logival View NV3

![ImplementationViewN3vsLogicalViewN4.svg](./Diagrams/NV3/VI-N4VLN3-Phase2.svg)

#### 2.3.3 Implementation View

![ImplementationViewN3.svg](./Diagrams/NV3/VI-NV3-Phase2.svg)

#### 2.3.4 Process View - UC3

##### ## Lending side of AMQP communication (Provider)

![ProcessViewN3-UC3-Provider.svg](./Diagrams/NV3/VP-NV3-UC3-PT1-Phase2.svg)

##### ## Recommendation side of AMQP communication (Consumer)

![ProcessViewN3-UC3-Provider.svg](./Diagrams/NV3/VP-NV3-UC3-PT2-Phase2.svg)








