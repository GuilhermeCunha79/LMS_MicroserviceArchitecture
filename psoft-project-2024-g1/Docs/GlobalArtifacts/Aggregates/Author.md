# Author

Wrote books that are present in the library. Can be main author, or co-author.

> [Attributes](#attributes)
>
> [Relevant Use Cases](#Relevant-Use-Cases)
>
> [Customer Clarifications](#Customer-Clarifications)

### Attributes

- Author Number
- [Name](../ValueObjects/Name.md)
- [Photo](../Entities/Photo.md)
- [Short bio](../ValueObjects/AuthorBio.md)

### Relevant Use Cases

- #### Phase 1
    - (WP#1A.3 : As Librarian I want to register an author (name, short bio))
    - (WP#1A.4 : As Librarian I want to update an author’s data)
    - (WP#1A.5 : As Librarian or Reader I want to know an author’s detail given its author number)
    - (WP#1A.6 : As Librarian or Reader I want to search authors by name)
- ### Phase 2
    - (WP#1B.3 : As Librarian I want to register an author with an optional photo)
    - (WP#1B.4 : As Reader I want to know the books of an Author)
    - (WP#1B.5 : As Reader I want to know the co-authors of an author and their respective books)
    - (WP#1B.6 : As Reader I want to know the Top 5 authors (which have the most lent books))


### Customer Clarifications

> [Q: Aproveito e pergunto sobre o número do autor, se este é apenas um numero e como este é formado?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28907#p36504)
>
>A: o numero do autor é um número sequencial gerado pelo sistema

> [Q: Que informações do autor é que o bibliotecário pode alterar?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28948#p36577)
>
>A: à exceção do "author number" pode alterar qualquer informação
