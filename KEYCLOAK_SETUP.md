# Configuração do Keycloak — Venda de Pamonhas

## Pré-requisitos
- Docker e Docker Compose instalados

## Iniciar o Keycloak

Execute:

```bash
docker-compose up -d
```

O Keycloak será iniciado automaticamente com a importação do realm configurado em:

```text
./keycloak
```

Aguarde alguns segundos para a inicialização concluir.

## Acesso ao Admin Console

- URL: http://localhost:8180
- Usuário: `admin`
- Senha: `admin`

## Realm Importado

O realm já é criado automaticamente durante a inicialização:

```text
venda-pamonhas
```

Também são importados automaticamente:

- Client: `venda-pamonhas-api`
- Roles:
  - `ROLE_USER`
  - `ROLE_ADMIN`

Não é necessário criar manualmente realms, clients ou roles.

## Configuração da Aplicação

Mantenha o OIDC habilitado:

```properties
quarkus.oidc.enabled=true
```

Configure o client secret utilizando o valor definido no realm importado:

```properties
quarkus.oidc.credentials.secret=OcL8kOstArIAD2oPlYeEbCMgL5i7EHuW
```

## Teste da Integração

Com o Keycloak e a aplicação em execução, ao realizar o cadastro de usuários através dos endpoints:

```http
POST /usuarios/cadastro-simples
POST /usuarios/cadastro-completo
```

o usuário será criado automaticamente no Keycloak e receberá a role:

```text
ROLE_USER
```

Para verificar:

1. Acesse o Admin Console
2. Entre no realm `venda-pamonhas`
3. Acesse **Users**
4. Localize o usuário cadastrado

## Observação

A autenticação local através de:

```http
POST /auth/login
```

continua funcionando normalmente.

Atualmente o Keycloak é utilizado para sincronização de usuários e permissões, facilitando uma futura migração completa da autenticação para OIDC.