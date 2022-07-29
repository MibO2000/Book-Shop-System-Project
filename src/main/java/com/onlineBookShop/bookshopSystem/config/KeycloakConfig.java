package com.onlineBookShop.bookshopSystem.config;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
@Component
public class KeycloakConfig {
    private final Environment env;

    public KeycloakConfig(Environment env) {
        this.env = env;
    }
    @Bean
    @Qualifier("Keycloak")
    public Keycloak getInstance(){
        return KeycloakBuilder.builder()
                .realm(env.getProperty("keycloak.realm"))
                .serverUrl(env.getProperty("keycloak.auth-server-url"))
                .clientSecret(env.getProperty("keycloak.credentials.secret"))
                .clientId("admin-cli")
                .username(env.getProperty("username"))
                .password(env.getProperty("password"))
                .build();
    }
}
