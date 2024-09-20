# Lending Number

A code that uniquely identifies a [Lending](../Aggregates/Lending.md).

> [Attributes](#attributes)
>
> [Relevant Use Cases](#Relevant-Use-Cases)
>
> [Customer Clarifications](#Customer-Clarifications)

### Attributes
- Registry Year
- Number


### Relevant Use Cases
- #### Phase 1
  - WP#2A.15 : As Librarian I want to lend a book to a reader. The lending can only be done if the reader has no
    overdue books and the reader can have at most 3 books lent. The return date must be presented
    to the Librarian
  - WP#2A.16 : As Reader I want to return a book. If the return is overdue I’m fined by the library.
  - WP#2A.17 : As Reader or Librarian I want to know the details of a lending given its lending number
- #### Phase 2
  - WP#4B.23 : As Librarian I want to list overdue lending sorted by their tardiness
  - WP#4B.14 : As Librarian I want to know the average number of lending per genre of a certain month
  - WP#4B.15 : As Librarian I want to know the Average lending duration

### Customer Clarifications

>[Q: O lending number é um id auto-gerado e auto-incrementado?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28894#p36740)
>
>A: O lending number é um número criado pelo sistema e composto pelo ano de registo e um número sequencial, ex., 2023/1, 2024/19876