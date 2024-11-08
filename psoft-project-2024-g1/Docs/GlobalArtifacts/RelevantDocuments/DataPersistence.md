
# Data Persistence ANalysis
Este documento descreve a configuração de repositórios no projeto, com foco em boas práticas e padrões de design aplicados para garantir flexibilidade, modularidade e facilidade de manutenção. O uso do Spring Framework para gestão da injeção de dependências promove a Inversão de Controlo (IoC) e a Separação de Preocupações, facilitando a evolução do sistema e a sua adaptabilidade a diferentes requisitos.

## 1. Serviço `AuthorServiceImpl`

A classe `AuthorServiceImpl` é responsável pela lógica de negócio relacionada aos autores, sendo a camada que orquestra a interação com os repositórios e outros serviços necessários. Esta implementação garante que a lógica de persistência de dados esteja desacoplada da lógica de negócio e que as implementações de repositórios possam ser alteradas sem impacto no código do serviço.

### Excerto de Código: Configuração do Serviço

```java
@Autowired
public AuthorServiceImpl(
        @Value("${author.repository.type}") String authorRepositoryType, 
        @Value("${book.repository.type}") String bookRepositoryType,
        ApplicationContext context, AuthorMapper mapper, PhotoRepository photoRepository, 
        @Value("${universal.generateID}") String generateId) {
    this.bookRepository = context.getBean(bookRepositoryType, BookRepository.class);
    this.mapper = mapper;
    this.photoRepository = photoRepository;
    this.authorRepository = context.getBean(authorRepositoryType, AuthorRepository.class);
    this.authorIDService = context.getBean(generateId, GenerateIDService.class);
}
```

### Boas Práticas e Padrões Utilizados

#### 1.1. **Inversão de Controlo (IoC)**
O uso do Spring para a injeção de dependências promove o desacoplamento entre as classes. Em vez de criar manualmente instâncias de repositórios e serviços, o Spring cuida da criação e configuração dos objetos, o que torna o sistema mais flexível e fácil de manter. Além disso, isso permite substituir facilmente implementações de repositórios em função de alterações de requisitos ou de tecnologia sem impactar diretamente o código da camada de serviço.

#### 1.2. **Injeção de Dependência com Valores Configuráveis**
Utilizando a anotação `@Value`, é possível configurar dinamicamente as dependências a serem injetadas, permitindo que as implementações de repositórios sejam especificadas através de valores no ficheiro `application.properties`. Este padrão de configuração externa proporciona maior flexibilidade e desacoplamento, pois as alterações podem ser feitas sem necessidade de modificar o código.

#### 1.3. **Uso de `ApplicationContext`**
O `ApplicationContext` é utilizado para recuperar dinamicamente as implementações de repositórios. Isso permite que o tipo de repositório seja escolhido em tempo de execução com base em configurações externas. Esta abordagem segue o padrão de **Factory Method** para a criação de instâncias, sem a necessidade de fazer uso direto de `new` nas classes.

---

## 2. Interface Genérica `AuthorRepository`

A interface `AuthorRepository` define as operações básicas de persistência que devem ser implementadas por qualquer repositório que lide com os dados de autores. O uso de interfaces promove a flexibilidade e facilita a troca de implementações de repositórios sem impactar o restante do código.

### Excerto de Código: Interface Genérica

```java
public interface AuthorRepository {

    Optional<Author> findByAuthorNumber(String authorNumber);

    List<Author> searchByNameNameStartsWith(String name);

    List<Author> searchByNameName(String name);

    Author save(Author author);

    Iterable<Author> findAll();

    Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageableRules);

    void delete(Author author);

    List<Author> findCoAuthorsByAuthorNumber(String authorNumber);
}
```

### Boas Práticas e Padrões Utilizados

#### 2.1. **Princípio da Substituição de Liskov**
A interface `AuthorRepository` assegura que qualquer implementação do repositório seja intercambiável sem alterar o comportamento do sistema. Este princípio é fundamental para garantir que implementações alternativas, como um repositório que persiste dados em uma base NoSQL, sejam suportadas sem modificar outras partes do sistema.

#### 2.2. **Interface Segregada**
A interface define apenas os métodos necessários para operações relacionadas ao autor, evitando que a implementação precise lidar com funcionalidades não relacionadas a esta entidade. Isso facilita a manutenção e a evolução do sistema, uma vez que mudanças nas operações de persistência de dados não afetam outras áreas da aplicação.

---

## 3. Implementação do Repositório `NoSQLAuthorRepository`

A implementação `NoSQLAuthorRepository` trata da persistência de dados no MongoDB. Utiliza o `MongoTemplate` para realizar consultas e manipulação de dados no banco NoSQL. A integração entre o repositório e o serviço `AuthorServiceImpl` é feita através da conversão de dados entre as entidades `AuthorMongo` e `Author`, utilizando um mapper.

### EXcerto de Código: Implementação do Repositório Mongo

```java
@Component("authorMongo")
@Primary
public class NoSQLAuthorRepository implements AuthorRepository {

    private final MongoTemplate mongoTemplate;
    private final AuthorMapper authorMapper;

    @Autowired
    public NoSQLAuthorRepository(MongoTemplate mongoTemplate, AuthorMapper authorMapper) {
        this.mongoTemplate = mongoTemplate;
        this.authorMapper = authorMapper;
    }

    @Override
    public Optional<Author> findByAuthorNumber(String authorNumber) {
        Query query = new Query(Criteria.where("authorNumber").is(authorNumber));
        Author author = authorMapper.toAuthor(mongoTemplate.findOne(query, AuthorMongo.class));

        return Optional.ofNullable(author);
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        Query query = new Query(Criteria.where("name").regex("^" + name));
        List<AuthorMongo> authorMongoList = mongoTemplate.find(query, AuthorMongo.class);

        return authorMongoList.stream()
                .map(authorMapper::toAuthor)
                .collect(Collectors.toList());
    }
}
```

### Boas Práticas e Padrões Utilizados

#### 3.1. **Padrão de Design Mapper**
O uso do `AuthorMapper` para converter entre as entidades `Author` e `AuthorMongo` segue o padrão de design **Mapper**, promovendo a separação de preocupações. Esta abordagem permite que a lógica de mapeamento de dados seja centralizada, facilitando alterações e extensões futuras, como a introdução de novas fontes de dados.

#### 3.2. **Uso de `MongoTemplate`**
A utilização do `MongoTemplate` permite que o repositório seja flexível e extensível, pois fornece uma interface rica para interagir com o MongoDB sem acoplar diretamente as classes do repositório aos detalhes da implementação de consultas. O repositório está preparado para realizar consultas eficientes e escaláveis, utilizando as capacidades nativas do MongoDB.

#### 3.3. **Componentização e @Primary**
A anotação `@Primary` indica que esta implementação de repositório deve ser a preferida quando o Spring precisa resolver uma dependência de `AuthorRepository`. A anotação `@Component("authorMongo")` permite que o Spring identifique a implementação correta para o tipo de repositório configurado no arquivo `application.properties`.

---

## 4. Configuração do `application.properties`

O arquivo `application.properties` é utilizado para configurar os repositórios em tempo de execução, permitindo que o tipo de repositório a ser utilizado seja determinado por variáveis de configuração. Isso garante que a escolha do repositório seja flexível e adaptável a diferentes contextos, sem a necessidade de modificar o código-fonte. A seguir, o codigo relevante para a configuração dos repositórios:

```properties
repository.suffix=Jpa

author.repository.type=author${repository.suffix}
book.repository.type=book${repository.suffix}
genre.repository.type=genre${repository.suffix}
lending.repository.type=lending${repository.suffix}
reader.repository.type=reader${repository.suffix}
user.repository.type=user${repository.suffix}
fine.repository.type=fine${repository.suffix}
```

Configura os tipos de repositórios para diferentes entidades no sistema, com o sufixo configurável através da variável `repository.suffix`. Isso permite alternar facilmente entre diferentes implementações de repositórios (por exemplo, JPA ou NoSQL) sem alterar o código, bastando ajustar a configuração no arquivo `application.properties`.

---

## Conclusão

A configuração dos repositórios em Spring, especialmente ao lidar com múltiplas fontes de dados, segue boas práticas de design e padrões como Inversão de Controlo, Padrão Factory e Mapper, e Substituição de Liskov. A flexibilidade proporcionada pela injeção de dependências e pela configuração externa permite que o sistema evolua de forma modular e sem acoplamento excessivo, facilitando a manutenção e a integração de novas tecnologias, como o MongoDB no caso apresentado.

As boas práticas discutidas ajudam a manter o código limpo, testável e fácil de escalar.


# Next

[Lending Recommendation Analysis Implementation](LendingRecommendation.md)
