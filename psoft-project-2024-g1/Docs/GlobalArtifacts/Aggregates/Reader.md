# Reader

Can request books for lending, and _should_ return them. Is a registered [user](../Entities/User.md) of the library. 

> [Attributes](#attributes)
> 
> [Relevant Use Cases](#Relevant-Use-Cases)
>
> [Customer Clarifications](#Customer-Clarifications)

### Attributes
- [Name](../ValueObjects/Name.md)
- [Email address](../ValueObjects/EmailAddress.md)
- [Birthdate](../ValueObjects/BirthDate.md)
- [Phone number](../ValueObjects/PhoneNumber.md)
- GDPR consent
- [Reader Number](../ValueObjects/ReaderNumber.md)
- [Username](../ValueObjects/Username.md)
- [Password](../ValueObjects/Password.md)
- [Photo](../Entities/Photo.md)

### Relevant Use Cases
- #### Phase 1
  - WP#1A.5 : As Librarian or Reader I want to know an author’s detail given its author number
  - WP#1A.6 : As Librarian or Reader I want to search authors by name
  - WP#2A.9 : As Librarian or Reader I want to know the details of a book given its ISBN
  - WP#2A.10 : As Librarian or Reader I want to search books by genre
  - WP#3A.11 : As anonymous I want to register as a reader (name, email, date of birth, phone number, GDPR
    consent). A Reader Number is assigned to me by the library.
  - WP#3A.12 : As Reader I want to update my personal data, e.g., phone number
    - A reader can only access (read/write) their own details. No reader can access the details of other readers.
  - WP#3A.13 : As Librarian I want to know a user’s detail given its reader number
  - WP#3A.14 : As Librarian I want to search Readers by name
  - WP#4A.15 : As Librarian I want to lend a book to a reader. The lending can only be done if the reader has no
    overdue books and the reader can have at most 3 books lent. The return date must be presented
    to the Librarian
  - WP#4A.16 : As Reader I want to return a book. If the return is overdue I’m fined by the library.
  - WP#4A.17 : As Reader or Librarian I want to know the details of a lending given its lending numbe
- #### Phase 2
  - WP#1B.4 : As Reader I want to know the books of an Author
  - WP#1B.5 : As Reader I want to know the co-authors of an author and their respective books
  - WP#1B.6 : As Reader I want to know the Top 5 authors (which have the most lent books)
  - WP#2B.8 : As Reader I want to search books by title
  - WP#3B.11 : As Librarian I want to know the Top 5 readers
  - WP#3B.12 : As anonymous I want to register as a reader with an optional photo, and optional list of interest
    (i.e., the genre I’m interested in). 
    - Refinement of use case from phase 1
  - WP#3B.13 : As Reader I want a list of book suggestions based on my interest list
  - WP#5B.17 : As Librarian I want to know the Top 5 readers per genre of a certain period
  - WP#5B.18 : As Librarian I want to know the Monthly lending per reader of a certain period

### Customer Clarifications
