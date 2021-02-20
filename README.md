# Chat Multicast Java

Aplicação de console para criação de chats usando Multicast.

**Aluno**: Guilherme Diniz  
**Professor**: Hugo de Paula

## Classes Utilizadas

- ClientApp: classe de entrada para a aplicação cliente que simplesmente checa por comandos no stdIn e os manda para a
  classe TCPClient.
- ServerApp: classe de entrada para a aplicação servidor que simplesmente pega a instância da ChatServer e faz um loop
  infinito esperando por novos clientes.
- TCPClient: cria a conexão TCP do cliente, conectando com o servidor e criar uma nova instância da ClientListenThread,
  que vai ler as respostas do servidor.
- ClientListenThread: simplesmente concatena as mensagens do servidor no stdOut do cliente.
- ChatMember: instância do membro de uma sala de chat.
- ChatRoom: instância de uma sala de chat.
- MulticastListenThread: thread de escuta de um membro por novas mensagens que chegarem em determinada sala.
- ChatRoomsManager: singleton de gerência das salas criadas (mantendo-as em uma lista encadeada).
- ChatServer: singleton do servidor TCP que simplesmente cria novas Connections para cada cliente.
- Connection: thread com toda a lógica de ouvir o cliente TCP e agir de acordo com os comandos que chegam.

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