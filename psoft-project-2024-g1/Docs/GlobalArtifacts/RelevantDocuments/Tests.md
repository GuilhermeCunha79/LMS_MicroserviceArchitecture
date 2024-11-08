# Documento de Testes

## Objetivo
Este documento descreve os testes realizados para garantir a qualidade do projeto, abordando diferentes tipos de testes, como **testes unitários de caixa branca e preta**, **testes de mutação**, **testes de integração**, e **testes de aceitação**. A implementação destes testes visa assegurar que o comportamento esperado do sistema é mantido, cobrindo cenários comuns e exceções.

---

## Tipos de Testes

### 1. **Testes de Caixa Branca (White Box Tests)**
Os testes de caixa branca verificam a lógica interna e a estrutura de implementação das classes de domínio. O objetivo é garantir que a construção e manipulação de objetos se comportem corretamente, conforme esperado.

**Exemplo de Teste:**
```java
@Test
void testConstructor() {
    assertEquals("John Doe", author.getName());
    assertEquals("Biography of John", author.getBio());
    assertEquals("photoURI", author.getPhoto().getPhotoFile());
}
```
Este teste verifica o construtor da classe `Author` para assegurar que os atributos `name`, `bio` e `photo` são inicializados corretamente.

**Exemplo de Teste de Exceção:**
```java
@Test
void ensureNameCannotBeNull() {
    assertThrows(IllegalArgumentException.class, () -> AuthorFactory.create(null, validBio, null));
}
```
Este teste verifica que, ao criar um `Author` com um nome nulo, é lançada uma exceção `IllegalArgumentException`. Assim, garantimos que a criação de autores inválidos é evitada.

---

### 2. **Testes de Caixa Preta (Black Box Tests)**
Os testes de caixa preta verificam a funcionalidade da aplicação sem considerar a implementação interna, focando apenas nas entradas e saídas esperadas.

**Exemplo de Teste:**
```java
@Test
void testCreateAuthorWithoutPhoto() {
    Author author = AuthorFactory.create(validName, validBio, null);
    assertNotNull(author);
    assertNull(author.getPhoto());
}
```
Neste teste, verificamos que a criação de um `Author` sem uma foto é permitida e que o objeto `photo` é nulo, como esperado.

---

### 3. **Testes de Mutação**
Os testes de mutação avaliam a robustez do código, introduzindo pequenas alterações e verificando se os testes conseguem identificar falhas. São importantes para garantir que o código é suficientemente robusto e que pequenas modificações são captadas.

**Exemplo de Teste:**
```java
@Test
void whenVersionIsStale_applyPatchThrowsStaleObjectStateExceptionMutation() {
    assertThrows(StaleObjectStateException.class, () -> author.applyPatch(999, request));
}
```
Este teste confirma que, ao tentar aplicar uma atualização com uma versão desatualizada, uma exceção `StaleObjectStateException` é lançada.

---

### 4. **Testes de Integração**
Os testes de integração validam a comunicação entre os diferentes componentes do sistema (controladores, serviços, repositórios e adaptadores).

**Exemplo de Teste de Integração:**
```java
@WithMockUser(username = "user", roles = {"ADMIN"})
@Test
public void testFindByAuthorNumber_Success() throws Exception {
    String authorNumber = "1";
    Author author = AuthorFactory.create("John Doe", "A famous author", "photo.jpg");
    AuthorView authorView = new AuthorView();
    authorView.setName("John Doe");
    authorView.setBio("A famous author");
    authorView.setPhoto("photo.jpg");

    given(authorService.findByAuthorNumber(authorNumber)).willReturn(Optional.of(author));
    given(authorViewMapper.toAuthorView(author)).willReturn(authorView);

    MvcResult result = mockMvc.perform(get("/api/authors/{authorNumber}", 1L)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    assertNotNull(responseBody);
    assertTrue(responseBody.contains("John Doe"));
    assertTrue(responseBody.contains("A famous author"));
    assertTrue(responseBody.contains("photo.jpg"));
}
```
Este teste garante que a API devolve com sucesso os detalhes de um autor, verificando se a resposta contém as informações corretas.

---

### 5. **Testes de Aceitação**
Os testes de aceitação validam se o sistema atende aos requisitos do cliente, testando o comportamento do sistema do ponto de vista do utilizador.

**Exemplo de Teste:**
```java
@Test
public void testFindByAuthorNumber_Success() {
    assertThat(this.authToken).isNotNull();

    String authorNumber = "1";
    String url = BASE_URL + "authors/" + authorNumber;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + this.authToken);

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<AuthorView> response = restTemplate.exchange(url, HttpMethod.GET, entity, AuthorView.class);
    AuthorView authorView = response.getBody();

    assertThat(authorView).isNotNull();
    assertThat(authorView.getName()).isEqualTo("Manuel Antonio Pina");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
}
```
Este teste assegura que a funcionalidade de obtenção de um autor específico está implementada corretamente, incluindo a autenticação com `Bearer token`.

---