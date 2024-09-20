# WP4A Lendings
## 1. Requirements Engineering


### 1.2. Customer Specifications and Clarifications

**From the [specifications document:](https://moodle.isep.ipp.pt/pluginfile.php/372607/mod_resource/content/0/PSOFT_LETI_assignment_2023-2024.pdf)**
>Central City library needs a system to manage their library, readers and book lending. The library consists
of thousands of books (no other media formats are available) organized by genre (e.g., Science-fiction,
mistery, Law, Medicine, etc.) that the readers can lend, take home and return after a period (typically 15
days). When a reader doesn’t return a book on time a fine will be applied per day of delay

**From the client clarifications:**

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

>[Q: Como é que a fine é aplicada?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28911#p36509)
>
>A: a aplicação da multa é tratada fora do sistema. o sistema deve calcular o valor da multa com base no numero de dias de atraso e num valor por dia. esse valor por dia deve ser facilmente parametrizavel nos sistema (ex., bootstrap ou ficheiro de propriedades)

>[Q: Quando um utilizador pretende devolver um livro, que informação é que o sistema lhe deve pedir?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=29057#p36737)
>
>A: deve ser dada a possibilidade de colocar um comentário/observações (opcional)

>[Q: O valor da multa - e se passou o prazo de devolução - é calculado aquando da consulta do empréstimo / momento de devolução, ou é algo a ser calculado periodicamente?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=29057#p36737)
>
>A: esta é uma decisão de implementação. de um ponto de vista funcional o importante é que quando se tenta devolver o livro o valor da multa seja o correto

>[Q: O valor da multa é algo a ser persistido na base de dados? Dos empréstimos cujos livros foram devolvidos; ou de todos, incluindo os em aberto?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=29057#p36737)
>
>A: sim. será posteriormente tratado por outra parte do sistema efetuar a cobrança (fora de âmbito)

>[Q: A multa tem um valor máximo?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=29057#p36737)
>
>A: não

>[Q: O lending number é um id auto-gerado e auto-incrementado](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28894#p36740)
>
>A: o lending number é um número criado pelo sistema e composto pelo ano de registo e um número sequencial, ex., 2023/1, 2024/19876

>[Q: Como é que a fine é aplicada?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28911#p36509)
>
>A: a aplicação da multa é tratada fora do sistema. o sistema deve calcular o valor da multa com base no numero de dias de atraso e num valor por dia. esse valor por dia deve ser facilmente parametrizavel nos sistema (ex., bootstrap ou ficheiro de propriedades)

>[Q: Qual é o comprimento máximo do comentário?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=29057#p37088)
>
>A: 1024 caracteres

### 1.4. Found out Dependencies

### 1.7. Functionality
### 1.8. Other Relevant Remarks
## 2. OO Analysis
### 2.1. Relevant Domain Model Excerpt
### 2.2. Other Remarks
## 3. Design
### 3.2. Class Diagram (CD)
## 4. Tests
## 5. Observations
