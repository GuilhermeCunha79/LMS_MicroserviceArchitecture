# WP #1B - Authors - As Reader I want to know the Top 5 authors which have the most lent books

## 1. Requirements Engineering
### 1.1. User Story Description

As Reader I want to know the Top 5 Book Lent (which have the most lent books)
### 1.2. Customer Specifications and Clarifications

>[View WP2B](../../../GlobalArtifacts/Aggregates/Book.md)


### 1.3. Acceptance Criteria
- returns the list of the 5 books that have been lent the most in the last year. it must return for each book, the number of times the book has been lent. the result must be sorted descending order.


### 1.4. Found out Dependencies
- Books 
- Lendings
- 
### 1.5 Input and Output Data

**Input Data:**

* Typed data:
   - request to find out the top 5 books lent

**Output Data:**

* Top 5 books 
* (In)success of the operation


## 3. Design
### 3.1. Sequence Diagram (SD)
![SD](Ph2-9-KnowTop5BooksLent.svg)
### 3.2. Class Diagram (CD)
![CD](../WP2B-Books-CD.svg)
## 4. Tests
## 5. Observations