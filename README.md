# Chat Cliente Servidor

## Configuração

Antes de iniciar o uso do chat, é necessário ter instalado o banco de dados [PostgreSQL](https://www.postgresql.org/).
Crie um banco de dados chamado `chat`:

```create database chat;```

Crie a seguinte tabela:

```
create table user (
  username varchar(255) not null unique,
  password varchar(255) not null
);
```

Após isto, configure a classe [Secrets.java]() com as credenciais criadas na instalação do postgres, deste modo validando a conexão.

OBS: recomenda-se uso da IDE IntelliJ para continuidade dos testes

## Como utilizar

- Depois de configurar o banco de dados, abra o código fonte e execute a classe [Server]();
- Execute também a classe [Login]()
- Após isto, volte para a IDE e clique no dropdown com o nome da classe `Login`, que estará localizado na parte superior da IDE, estando também a esquerda do botão `run`, e clique em `Edit Configurations...`:

![image](https://github.com/AllanSouza23/chat-aps/assets/64336814/fc91b080-15e4-47b9-8a73-37eb651b1e0b)

- Clique em `Modify options` destacada em azul, e clique em `Allow multiple instances`, por fim também clique em `Apply` e depois em `Ok` 

![image](https://github.com/AllanSouza23/chat-aps/assets/64336814/8821b368-dc23-4855-94ff-0e4401203a9d)

- Agora você pode voltar à tela inicial anteriormente disposta, e clicar em `Criar novo usuario`: 

![image](https://github.com/AllanSouza23/chat-aps/assets/64336814/b38354fe-ed34-4156-8a4e-b883d9af202d)

- Registre o novo usuario e clique em `Criar`

![image](https://github.com/AllanSouza23/chat-aps/assets/64336814/aa933139-bd2f-4d02-9e85-a7a35871bbea)

- Agora novamente na tela de login, forneça as credenciais cadastradas para o usuario, uma porta valida (recomenda-se escolher valores abaixo de 5000) e clique em entrar. Deverá estar disposto uma tela informando contatos como a apresentada a seguir:

![image](https://github.com/AllanSouza23/chat-aps/assets/64336814/4112a953-7a6a-4634-b3c7-91a5c832cba7)

- Quando houver mais de um usuário clique em `Atualizar contatos` para receber do listener o novo contato online, devendo expor da seguinte forma:

![image](https://github.com/AllanSouza23/chat-aps/assets/64336814/7e63ab12-bcc6-4fc5-a0ab-5bf773604767)

- Abra a conversa e envie textos entre os usuários:

![image](https://github.com/AllanSouza23/chat-aps/assets/64336814/f6216be6-bb82-4106-8f81-f9da4b636da9)

