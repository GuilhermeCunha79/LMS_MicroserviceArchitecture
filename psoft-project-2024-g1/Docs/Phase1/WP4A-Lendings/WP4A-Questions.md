# WP4A

## Answered:
>[Q: Será possível fazer um empréstimo de vários livros a um leitor? Ou cada livro emprestado, corresponde a um só empréstimo?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28893#p36489)
>
>A: um empréstimo é apenas de um único livro
> 

>[Q: Como é que a fine é aplicada?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28911#p36509)
>
>A: a aplicação da multa é tratada fora do sistema. o sistema deve calcular o valor da multa com base no numero de dias de atraso e num valor por dia. esse valor por dia deve ser facilmente parametrizavel nos sistema (ex., bootstrap ou ficheiro de propriedades)


## Open

>### US15:
>
>As Librarian, I want to lend a book to a reader. The lending can only be done if the reader has no
overdue books and the reader can have at most 3 books lent. The return date must be presented
to the Librarian

>### US16:
>
>As Reader, I want to return a book. If the return is overdue I’m fined by the library.
>
>1. Quando um utilizador pretende devolver um livro, que informação é que o sistema lhe deve pedir?
>
>2. O valor da multa - e se passou o prazo de devolução - é calculado aquando da consulta do empréstimo / momento de devolução, ou é algo a ser calculado periodicamente?
>
>3. O valor da multa é algo a ser persistido na base de dados? Dos empréstimos cujos livros foram devolvidos; ou de todos, incluindo os em aberto?
>
>4. A multa tem um valor máximo?

>### US17:
>As Reader or Librarian I want to know the details of a lending given its lending number
>


