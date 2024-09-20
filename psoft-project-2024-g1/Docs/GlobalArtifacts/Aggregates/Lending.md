# Lending

Is a transaction, managed by a Librarian, relating a Book loan to a Reader.

> [Attributes](#attributes)
>
> [Relevant Use Cases](#Relevant-Use-Cases)
>
> [Customer Clarifications](#Customer-Clarifications)

### Attributes
- [LendingNumber](../ValueObjects/LendingNumber.md)
- [Book](Book.md)
- [Reader](Reader.md)
- Start date
- Limit date
- Returned date

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
>[Q: O tempo limite de entrega de um livro é expectável que mude? ](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28903#p36499)
>
>A: o numero de dias que um livro pode ser requisitado é um valor fixo que deve ser facilmente configuravel (ex., ficheiro de propriedades, bootstraping)

>[Q: Será possível fazer um empréstimo de vários livros a um leitor? Ou cada livro emprestado, corresponde a um só empréstimo?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28893#p36489)
>
>A: um empréstimo é apenas de um único livro

>[Q: Relativamente a data de entrega, ficamos pelos dias ou temos de contabilizar as horas?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28903#p36499)
>
>A: apenas devem contabilizar dias

>[Q:Deve ser possível que o leitor e o bibliotecário tenham acesso aos detalhes de um empréstimo através do seu número.
Pode especificar quais são os detalhes que devem ser facultados? Existe alguma diferença nos detalhes a serem facultados, caso seja um leitor ou um bibliotecário a solicitar?
](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28894#p36490)
>
>A: a ambos deve ser facultado:
>- lending number
>- book title
>- lending date
>- return date
>- number of days till return date (if applicable)
>- number of days in overdue (if applicable)
>- fine amount (if applicable)

>[Q: Como verificamos que um livro está disponível para ser requisitado?
](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28882#p36478)
>
>A: [...] O sistema nao necessita controlar o numero de exemplares de cada livro nem a sua disponibilidade

> [Q: Qual é o comprimento máximo do comentário?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=29057#p37088)
>
>A: 1024 caracteres

