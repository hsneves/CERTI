# CERTI
CERTI - Desafio Catch them all!

- Sistemas:
  - certi-test-server.jar
    - Backend utilizando Spring Boot, REST, Server-Sent Events, JPA, etc. É consumido pelos outros dois sistemas via serviços REST para captura e consulta dos Pokeons que já foram capturados. Após cada captura o Pokemon é armazenado em um banco de dados SQLite local por JPA. Utiliza Server-Sent Events para manter um canal de comunicação entre o servidor e os aplicativos, onde é possível visualizar em cada aplicativo quando um Pokemon é capturado por outro aplicativo, atualizando sua lista de pokemons capturados. Possui uma interface simples com uma tabela apresentando os pokemons, quantos foram capturados e a data/hora da última captura. Está abrindo o servidor na porta 3000, usuário: admin, senha: admin

  - certi-cta-angular.zip
    - Frontend em angular para consumir os serviços REST do backend e cliente do Server-Sent Evens.

  - certi-cta-flutter.zip
    - Frontend em flutter para consumir os serviços REST do backend e cliente do Server-Sent Evens.

- showcase:
  - certi-cta.mkv
    - Video demonstrando o funcionamento de todos os sistemas interligados.

- build: contém os builds do desafio.
  - certi-cta-angular.zip (Angular, compactado)
  - certi-cta-flutter-windows.zip (flutter, executável windows)
  - certi-cta-server.jar (Spring Boot, java -jar certi-cta-server.jar)
