package com.samuel.itau.desafio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Itaú Transaction API")
                        .version("1.0.0")
                        .description("API para gerenciamento de transações financeiras, permitindo adicionar transações, "
                                + "obter estatísticas e excluir todas as transações. A API valida os dados de entrada e "
                                + "mantém estatísticas atualizadas das transações dos últimos 60 segundos."));
    }
}