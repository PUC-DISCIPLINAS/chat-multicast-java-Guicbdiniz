# Chat Multicast Java

Aplicação de console para criação de chats usando Multicast.

**Aluno**: Guilherme Diniz  
**Professor**: Hugo de Paula

## Classes Utilizadas

- ClientApp: classe de entrada para a aplicação cliente que simplesmente checa por comandos no stdIn e os manda para a
  classe TCPClient.
- ServerApp: classe de entrada para a aplicação servidor que
- TCPClient:
- ClientListenThread:
- ChatMember:
- ChatRoom:
- MulticastListenThread:
- ChatRoomsManager:
- ChatServer:
- Connection:

## Protocolo Utilizado

Para comunicação TCP entre a aplicação cliente e aplicação servidor, eu criei um protocolo simples que aceitava os
seguintes comandos:

- login *username*: comando inicial do protocolo para logar um cliente com o nome escolhido.
- list: comando para listar as salas disponíveis para conexão.
- create *roomname*: comando para criar uma sala com o nome escolhido.
- enter *roomname*: comando para entrar em determinada sala.
- send *roomname* *message*\*: comando para enviar ume mensagem (* de tamanho qualquer) para determinada sala.
- leave *roomname*: comando para sair de determinada sala.
- help: comando para imprimir possíveis comandos na tela.
- exit: comando para fechar a aplicação.