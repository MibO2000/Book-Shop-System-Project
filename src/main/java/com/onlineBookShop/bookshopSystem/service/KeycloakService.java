package com.onlineBookShop.bookshopSystem.service;

import com.onlineBookShop.bookshopSystem.entity.User;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {
    UserRepresentation createUser(User user);
    String getUserKeycloakId();
}
