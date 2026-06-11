package br.unitins.tp1.projeto.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

// Os valores de info são sobrescritos pelas propriedades quarkus.smallrye-openapi.info-* do application.properties
@OpenAPIDefinition(
    info = @Info(title = "API Venda de Pamonhas", version = "1.0.0"),
    tags = {
        @Tag(name = "Autenticação",        description = "Autenticação de usuários e geração de tokens JWT"),
        @Tag(name = "Recuperação de Senha", description = "Fluxo de solicitação e redefinição de senha por e-mail"),
        @Tag(name = "Usuários",            description = "Operações relacionadas ao cadastro e gerenciamento de usuários"),
        @Tag(name = "Pamonhas",            description = "Operações relacionadas a pamonhas do e-commerce"),
        @Tag(name = "Categorias",          description = "Operações relacionadas a categorias de pamonhas"),
        @Tag(name = "Ingredientes",        description = "Operações relacionadas a ingredientes e controle de estoque"),
        @Tag(name = "Modos de Preparo",    description = "Operações relacionadas a modos de preparo de pamonhas"),
        @Tag(name = "Embalagens",          description = "Operações relacionadas a embalagens (plásticas e biodegradáveis)"),
        @Tag(name = "Unidades de Medida",  description = "Consulta das unidades de medida disponíveis no sistema"),
        @Tag(name = "Cupons de Desconto",  description = "Operações relacionadas a cupons de desconto"),
        @Tag(name = "Lista de Desejos",    description = "Operações relacionadas à lista de desejos do usuário autenticado"),
        @Tag(name = "Endereços",           description = "Operações relacionadas aos endereços do usuário autenticado"),
        @Tag(name = "Estados",             description = "Operações relacionadas a estados"),
        @Tag(name = "Cidades",             description = "Operações relacionadas a cidades"),
        @Tag(name = "Pedidos",             description = "Operações relacionadas a pedidos do e-commerce"),
        @Tag(name = "Status de Pedido",    description = "Consulta dos status de pedido disponíveis no sistema")
    },
    security = @SecurityRequirement(name = "jwt")
)
@SecurityScheme(
    securitySchemeName = "jwt",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiConfig {
}
