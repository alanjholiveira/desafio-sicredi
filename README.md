# Desafio Sicredi


No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. Api desenvolvida para gerenciar sessões de votação.

Essa API REST é possível:

* Cadastrar uma nova pauta.
* Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um
tempo determinado na chamada de abertura ou 1 minuto por default).
* Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada
associado é identificado por um id único e pode votar apenas uma vez por pauta).
* Contabilizar os votos e dar o resultado da votação na pauta.


! Qualquer chamada para as interfaces pode ser considerada como autorizada !

# Tecnologias utilizadas
 - Spring boot
 - Junit 5
 - Mockito
 - Rest Assured
 - Java 11
 - testContainer
 - Feign Client
 - PostgreSQL
 - Liquibase
 - RabbitMQ
 - Lombok
 - Swagger v3
 - Docker
 - Docker Compose

# Tarefas bônus
- Integração com sistema externos
- Messageria e filas utilizando
- Perfomace
- Versionamento da API
 
# Método de desenvolvimento
- Utilizado banco de dados PostgreSQL para salvar informações da aplicação
- Implementado um agendador que executa a cada 30min a procura de uma sessão fechada para contabilizar
  os votos e em seguida prepara evento para ser enviado para fila
- Utilizado Swagger v3 para documentar api rest
- Implementado testContainer para utilização dos teste unitário e integrado

# Modos de execução

#### 1 - Para executar projeto local é necessário possuir o docker é docker-compose instalado.

Passo 1:

```
docker-compose up -d --build
```

Passo 2:

```
http://localhost:8080/swagger-ui/#/
```
