# WP3B Readers
## 1. Requirements Engineering


### 1.1. Customer Specifications and Clarifications

**From the [specifications document:](https://moodle.isep.ipp.pt/pluginfile.php/372607/mod_resource/content/0/PSOFT_LETI_assignment_2023-2024.pdf)**
>Central City library needs a system to manage their library, readers and book lending. The library consists
of thousands of books (no other media formats are available) organized by genre (e.g., Science-fiction,
mistery, Law, Medicine, etc.) that the readers can lend, take home and return after a period (typically 15
days). When a reader doesn’t return a book on time a fine will be applied per day of delay

**From the client clarifications:**

> Q: Quais são os géneros a ser considerados para a lista de interesses? Os géneros disponíveis para os books?
>
> A: apenas pode escolher géneros da lista definida no WP0A.2

> Q: O que é que define o TOP 5 de readers? A quantidade de livros emprestados ao reader? Devem ser tidos em conta mais métodos de sorting para além do top5 por lendings (critério e/ou quantidade de resultados)? (possivel implementação desses métodos no futuro)
>
> A: os "5 top readers" são os leitores que mais livros requisitaram no último ano. Não se preve outros métodos no futuro

> Q: "Quais são os critérios para a sugestão de livros?"
> 
> A: "A sugestão de livros é feita com base nos géneros que o leitor tem na sua lista de interesses. A sugestão é feita de forma aleatória, mas garantindo que o livro sugerido pertence a um dos géneros da lista de interesses do leitor. Os livros sugeridos são aleatórios"

### 1.2. Found out Dependencies
- None

## 2. Design
### 2.1. Class Diagram (CD)

![CD](./WP3B-Readers-CD.svg)
