# Book

Is an item stored in the library, and is lent to readers, by librarians.

> [Attributes](#attributes)
>
> [Relevant Use Cases](#Relevant-Use-Cases)
>
> [Customer Clarifications](#Customer-Clarifications)

### Attributes
- [ISBN](../ValueObjects/ISBN.md)
- [Title](../ValueObjects/Title.md)
- [Genre](../Entities/Genre.md)
- [Description](../ValueObjects/Description.md)
- [Author](Author.md)(s)

### Relevant Use Cases
- #### Phase 1
  - WP#2A.7 : As Librarian, I want to register a book (isbn, title, genre, description, author(s))
  - WP#2A.8 : As Librarian I want to update a book’s data
  - WP#2A.9 : As Librarian or Reader I want to know the details of a book given its ISBN
  - WP#2A.10 : As Librarian or Reader I want to search books by genre
  - WP#4A.15 : As Librarian I want to lend a book to a reader. The lending can only be done if the reader has no
    overdue books and the reader can have at most 3 books lent. The return date must be presented
    to the Librarian
  - WP#4A.16 : As Reader I want to return a book. If the return is overdue I’m fined by the library.
- #### Phase 2
  - WP#1B.4 : As Reader I want to know the books of an Author
  - WP#1B.5 : As Reader I want to know the co-authors of an author and their respective books
  - WP#1B.6 : As Reader I want to know the Top 5 authors (which have the most lent books)
  - WP#2B.7 : As Librarian, I want to register a book with a book cover photo
    - Refinement of use case from phase 1
  - WP#2B.8 : As Reader I want to search books by title
  - WP#2B.9 : As Librarian I want to know the Top 5 books lent
  - WP#3B.13 : As Reader I want a list of book suggestions based on my interest list

### Customer Clarifications
>[Q: Quais são os critério de aceitação (acceptance criteria) da us07?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28891#p36487)
>
>A: Se tentar registar um livro com um ISBN já existente deve ser indicado um erro.
> Titulo do livro é obrigatório e não pode comecar ou terminar em espaços.
> Descrição é opcional e deve suportar conteudo HTML.
> Género e autor são obrigatórios.

>[Q: Qual o dado que precisamos de introduzir para proceder à atualização dos dados de um livro?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28929#p36537)
>
>A: À execção do ISBN todos os dados sao alteraveis

>[Q: um livro pode ter mais que um género?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=29071#p36760)
>
>A: (Ricardo - respondido abaixo)

>[Q: Sobre a pergunta 3, creio que ainda não ficou claro se os 'genre's são escolhidos pelo 'librarian' (na hora de registar um livro) de uma lista existente (e eventualmente mutável), ou se são escritos manualmente pelo 'librarian'.](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28911#p36699)
>
>A: Quando o Librarian regista um novo livro vai indicar **em que genéro** esse livro se classifica. só poderá indicar um dos géneros previamente registados no sistema