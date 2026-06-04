# Configuração do Keycloak — Venda de Pamonhas

## Pré-requisitos
- Docker e Docker Compose instalados

## 1. Subir os serviços
```bash
docker-compose up -d
```

Aguarde ~30 segundos para o Keycloak inicializar completamente.

## 2. Acessar o Admin Console
- URL: http://localhost:8180
- Usuário: `admin`
- Senha: `admin`

## 3. Criar o Realm
1. Clique no menu superior esquerdo (onde aparece "Keycloak")
2. Clique em **"Create realm"**
3. Em **Realm name**, digite: `venda-pamonhas`
4. Clique em **"Create"**

## 4. Criar o Client
1. No menu lateral, clique em **Clients**
2. Clique em **"Create client"**
3. Preencha:
   - **Client type:** OpenID Connect
   - **Client ID:** `venda-pamonhas-api`
4. Clique em **"Next"**
5. Em **Capability config**:
   - Ative **"Client authentication"**
   - Mantenha **"Authorization"** desativado
6. Clique em **"Save"**
7. Vá na aba **"Credentials"** e copie o **Client secret**
8. Atualize o `application.properties`:
   ```properties
   quarkus.oidc.credentials.secret=SEU_SECRET_AQUI
   ```

## 5. Criar as Roles
1. No menu lateral, clique em **Realm roles**
2. Clique em **"Create role"**
3. Crie as duas roles:
   - `ROLE_USER`
   - `ROLE_ADMIN`

## 6. Habilitar o OIDC na aplicação
No `application.properties`, altere:
```properties
quarkus.oidc.enabled=true
```

## 7. Testar a integração
Após subir a aplicação com Keycloak rodando, ao fazer um cadastro de usuário
via `POST /usuarios/cadastro-simples` ou `POST /usuarios/cadastro-completo`,
o usuário será automaticamente criado no Keycloak com a role `ROLE_USER`.

Para verificar:
1. Acesse o Admin Console → Users
2. O usuário recém-cadastrado deve aparecer na lista

## Observação importante
A autenticação local (`POST /auth/login`) continua funcionando normalmente
independente do Keycloak. O Keycloak é usado apenas para sincronização
de usuários e senhas, permitindo que no futuro o login possa ser migrado
para o Keycloak completamente.
