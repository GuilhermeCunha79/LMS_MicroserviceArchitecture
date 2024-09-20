# Librarian

Is a library employee. Manages Authors', Readers' and Books' data. Lends books to Readers. Is a registered [user](User.md) of the library.

> [Attributes](#attributes)
>
> [Relevant Use Cases](#Relevant-Use-Cases)
>
> [Customer Clarifications](#Customer-Clarifications)

### Attributes
- [Email address](../ValueObjects/EmailAddress.md)
- [Name](../ValueObjects/Name.md)
- [Username](../ValueObjects/Username.md)
- [Password](../ValueObjects/Password.md)



### Relevant Use Cases
- #### Phase 1
    - (WP#1A.xxx : As Librarian ...)


### Customer Clarifications
>[Q: Quais são os critério de aceitação (acceptance criteria) da us07?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28891#p36487)
>
>A:Se tentar registar um livro com um ISBN já existente deve ser indicado um erro.
ISBN usamos o formato ISBN-10 ou ISBN-13. 
Titulo do livro é obrigatório e não pode comecar ou terminar em espaços.
Descrição é opcional e deve suportar conteudo HTML.
Género e autor são obrigatórios

>[Q: Gostaria de saber quais são as caracteristicas necessários para um User fazer autenticação no sistema?](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28878#p36474)
>
>A: Os utilizadores autenticam-se usando um par username/password. [...] Quer os bibliotecários, quer os leitores são utilizadores do sistema

>[Q: O librarian tem a mesma user info que a do reader? (Name, E-mail, Phone Number, etc)](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28911#p36509)
> 
>A: os bibliotecários são funcionários da biblioteca e a sua informação pessoal é gerida no sistema de processamento de salários e não neste sistema.

>[Q: Gostaríamos de saber quais os atributos que a classe Librarian deve possuir. ](https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=28797#p36464)
>
>A: Neste caso o Bibliotecário é um utilizador do sistema e apenas necessitamos de saber o seu nome e email, obviamente juntamente com a sua informação de autenticação no sistema.