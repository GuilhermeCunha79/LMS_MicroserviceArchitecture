Claro! Aqui está o texto em formato Markdown para você copiar:

markdown
Copiar código

# Serviço de Recomendação de Empréstimos

Este serviço é responsável pela recomendação de livros para os leitores com base em critérios configuráveis. A lógica de
recomendação é aplicada de acordo com a disponibilidade de livros e a popularidade de géneros, além de outros
parâmetros, como a idade mínima e a faixa etária adulta, configurados externamente.

O uso de boas práticas de design no desenvolvimento deste serviço permite flexibilidade e facilidade de manutenção, além
de garantir que os requisitos de recomendação possam ser ajustados sem impacto direto no código. A aplicação de padrões
como Inversão de Controlo, injeção de dependências e a separação de responsabilidades entre os componentes torna o
sistema mais modular e escalável.

## 1. Serviço LendingServiceImpl

A classe LendingServiceImpl lida com a lógica de negócio dos empréstimos, incluindo a recomendação de livros com base
nos critérios configurados. O serviço integra os repositórios de livros, leitores, multas e empréstimos para realizar as
operações necessárias. O serviço de recomendação de empréstimos é configurado através de valores no arquivo de
propriedades e invocado dinamicamente, permitindo diferentes comportamentos com base nas especificações de configuração.

### Excerto de Código: Configuração do Serviço

```java
@Autowired
public LendingServiceImpl(@Value("${reader.repository.type}") String readerRepositoryType,
@Value("${book.repository.type}") String bookRepositoryType,
        ApplicationContext context,
@Value("${lending.repository.type}") String lendingRepositoryType,
@Value("${fine.repository.type}") String fineRepositoryType,
        LendingFactory lendingFactory,
@Value("${universal.lendingRecommendation.algorithm}") String beanContext,
@Value("${universal.generateID}") String generateId){
        this.readerRepository=context.getBean(readerRepositoryType,ReaderRepository.class);
        this.lendingFactory=lendingFactory;
        this.lendingRepository=context.getBean(lendingRepositoryType,LendingRepository.class);
        this.fineRepository=context.getBean(fineRepositoryType,FineRepository.class);
        this.bookRepository=context.getBean(bookRepositoryType,BookRepository.class);
        this.lendingRecommendation=context.getBean(beanContext,LendingRecommendation.class);
        this.generateIDService=context.getBean(generateId,GenerateIDService.class);
        }
```

### Boas Práticas e Padrões Utilizados

#### 1.1. Inversão de Controlo (IoC)

O uso do Spring para a injeção de dependências permite o desacoplamento entre as classes, evitando a necessidade de
criação manual de instâncias de repositórios e serviços. Com isso, o sistema se torna mais flexível, permitindo a troca
de implementações de repositórios e serviços sem afetar a lógica de negócio.

#### 1.2. Configuração Externa via application.properties

A configuração dos parâmetros de recomendação, como o algoritmo, as faixas etárias e a quantidade de livros a serem
recomendados, é feita através do arquivo application.properties. Isso garante que os comportamentos do sistema possam
ser ajustados sem a necessidade de modificar o código-fonte. O uso de @Value para injeção de propriedades permite a
personalização dinâmica do comportamento do serviço, facilitando a manutenção e adaptação do sistema a novas
necessidades.

#### 1.3. Uso de ApplicationContext

A utilização do ApplicationContext para resolver as dependências de repositórios e serviços em tempo de execução permite
que a implementação de determinados comportamentos, como o algoritmo de recomendação de empréstimos, seja decidida com
base nas configurações externas. Este padrão segue a abordagem de Factory Method, permitindo a criação e configuração
dos beans de maneira dinâmica, sem necessidade de instanciá-los diretamente.

---

## 2. Serviço de Recomendação de Empréstimos LendingRecommendation

A interface _LendingRecommendation_ define o contrato para o serviço de recomendação de livros. O algoritmo de
recomendação pode ser alterado dinamicamente de acordo com o valor configurado em application.properties. O serviço de
recomendação é chamado dentro do LendingServiceImpl para recomendar livros aos leitores com base nas configurações de
entrada.

### Excerto de Código: Interface LendingRecommendation

```java
public interface LendingRecommendation {
    Iterable<LendingView> bookRecommendation(final CreateLendingRequest resource);
}
```

### Excerto de Código: Implementação da Recomendação de Livros

```java
@Override
public Iterable<LendingView> bookRecommendation(final CreateLendingRequest resource) {
    List<LendingView> lendingList = new ArrayList<>();

    List<Book> books = this.bookRepository.findXBooksByYGenre(x, y);

    final var r = readerRepository.findByReaderNumber(resource.getReaderNumber())
            .orElseThrow(() -> new NotFoundException("Reader not found"));

    books.forEach(book -> {
        Lending newLending = LendingFactory.create(
                book,
                r,
                lendingRepository.getCountFromCurrentYear() + 1,
                lendingDurationInDays,
                fineValuePerDayInCents
        );
        lendingList.add(lendingViewMapper.toLendingView(newLending));
    });

    return lendingList;
}
```

### Boas Práticas e Padrões Utilizados

#### 2.1. Padrão de Design de Estratégia (Strategy)
O algoritmo de recomendação é selecionado dinamicamente através da configuração universal.lendingRecommendation.algorithm. Isso permite que o sistema altere o comportamento da recomendação sem modificar a implementação do serviço. A troca de algoritmos é transparente para o restante do sistema, respeitando o padrão Strategy, que define uma família de algoritmos e permite que o cliente escolha qual usar.

#### 2.2. Princípio da Injeção de Dependência
A dependência do LendingRecommendation é resolvida automaticamente pelo Spring, com base nas configurações externas. Isso permite que o sistema seja facilmente configurado para utilizar diferentes estratégias de recomendação conforme as necessidades do negócio.

#### 2.3. Consulta Dinâmica de Livros com Base em géneros
A consulta aos livros é realizada de maneira dinâmica, considerando a popularidade dos géneros e a quantidade de livros a serem recomendados. Isso é feito utilizando o método findXBooksByYGenre, que executa uma consulta SQL para obter livros de géneros mais populares, com base no número de livros por género. Esse tipo de consulta oferece flexibilidade na recomendação de livros e pode ser facilmente ajustado conforme novos requisitos.

---

## 3. Implementação da Consulta de Livros por géneros findXBooksByYGenre
   O método findXBooksByYGenre realiza uma consulta ao banco de dados para retornar uma lista de livros de géneros populares, com base nas quantidades configuradas em universal.lendingRecommendation.x e universal.lendingRecommendation.y. A consulta é otimizada para retornar os livros mais relevantes de forma eficiente.

### Excerto de Código: Consulta de Livros por géneros

```java
@Override
public List<Book> findXBooksByYGenre(@Param("x") int x, @Param("y") int y) {
    return entityManager.createNativeQuery(
                    "SELECT b.* " +
                            "FROM ( " +
                            "    SELECT b.*, " +
                            "           ROW_NUMBER() OVER (PARTITION BY b.GENRE_PK ORDER BY b.TITLE) AS rn " +
                            "    FROM Book b " +
                            "    JOIN Genre g ON b.GENRE_PK = g.PK " +
                            "    WHERE g.PK IN ( " +
                            "        SELECT g.PK " +
                            "        FROM Genre g " +
                            "        JOIN Book b ON b.GENRE_PK = g.PK " +
                            "        GROUP BY g.PK " +
                            "        ORDER BY COUNT(b.PK) DESC " +
                            "        LIMIT :y " +
                            "    ) " +
                            ") AS b " +
                            "WHERE rn <= :x " +
                            "ORDER BY b.GENRE_PK, b.TITLE;",
                    Book.class)
            .setParameter("x", x)
            .setParameter("y", y)
            .getResultList();
}
```

### Boas Práticas e Padrões Utilizados

#### 3.1. Uso de SQL Nativo para Consultas Complexas
A consulta SQL nativa é utilizada para retornar livros de géneros populares com base nas quantidades configuradas. A utilização de SQL nativo permite otimizar consultas complexas, como o uso de ROW_NUMBER() para particionar os livros por géneros e obter os mais relevantes. Essa abordagem proporciona maior controle sobre as consultas e melhora a performance ao lidar com grandes volumes de dados.

#### 3.2. Uso de Parâmetros Dinâmicos na Consulta
Os parâmetros x e y são passados dinamicamente para a consulta, permitindo a personalização da quantidade de livros a serem retornados com base nas configurações. Isso proporciona flexibilidade no comportamento da consulta e facilita a manutenção do código à medida que novos parâmetros ou requisitos são adicionados.

---

## Conclusão
O serviço de recomendação de empréstimos segue boas práticas e padrões de design, como a Injeção de Dependência, o padrão de Strategy para escolha de algoritmos e o uso de consultas SQL otimizadas. A configuração externa e a flexibilidade na escolha dos comportamentos do sistema garantem que o serviço seja adaptável e fácil de manter. Além disso, a separação de responsabilidades e a modularidade do código tornam o sistema escalável e pronto para evoluir conforme as necessidades de negócios mudam.


