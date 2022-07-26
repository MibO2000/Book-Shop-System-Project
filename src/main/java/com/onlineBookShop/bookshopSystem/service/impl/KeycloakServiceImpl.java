package com.onlineBookShop.bookshopSystem.service.impl;

import com.onlineBookShop.bookshopSystem.constants.Util;
import com.onlineBookShop.bookshopSystem.entity.User;
import com.onlineBookShop.bookshopSystem.service.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.*;

@Service
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    private final Environment environment;
    private final Keycloak keycloak;
    private final HttpServletRequest httpServletRequest;
    private final Util util;

    public KeycloakServiceImpl(Environment environment, @Qualifier("Keycloak") Keycloak keycloak, HttpServletRequest httpServletRequest, Util util) {
        this.environment = environment;
        this.keycloak = keycloak;
        this.httpServletRequest = httpServletRequest;
        this.util = util;
    }

    private String getRealm() {
        return environment.getProperty("keycloak.realm");
    }

    private String getClientId() {
        return environment.getProperty("keycloak.resource");
    }

    private static CredentialRepresentation createPasswordCredentials() {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(true);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue("1234");
        return passwordCredentials;
    }

    public void addRealmRoleToUser(String userName, String role_name) {
        log.info("AddRealmRoleToUser : {}", userName + " " + role_name);
        String client_id = keycloak.realm(getRealm()).clients().findByClientId(getClientId()).get(0).getId();

        String userId = keycloak.realm(getRealm()).users().search(userName).get(0).getId();
        UserResource user = keycloak.realm(getRealm()).users().get(userId);
        List<RoleRepresentation> roleToAdd = new LinkedList<>();
        roleToAdd.add(keycloak.realm(getRealm()).clients().get(client_id).roles().get(role_name).toRepresentation());
        List<RoleRepresentation> roleRepresentationList = new ArrayList<>();
        roleRepresentationList.add(keycloak.realm(getRealm()).roles().get(role_name).toRepresentation());
        user.roles().clientLevel(client_id).add(roleToAdd);
        user.roles().realmLevel().add(roleRepresentationList);
    }

    @Override
    public UserRepresentation createUser(User user) {
        UsersResource usersResource = keycloak.realm(getRealm()).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials();
        UserRepresentation newUser = new UserRepresentation();
        newUser.setCredentials(Collections.singletonList(credentialRepresentation));
        newUser.setUsername(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setEnabled(true);
        newUser.setEmailVerified(false);
        List<String> roleList = new ArrayList<>();
        roleList.add("user");
        newUser.setRealmRoles(roleList);
        try {
            Response response = usersResource.create(newUser);
            log.info("Response Status : {}", response.getStatus());
            if (response.getStatus() == 201) {
                List<UserRepresentation> search = keycloak.realm(getRealm()).users().search(newUser.getUsername());
                newUser.setId(search.get(0).getId());
                addRealmRoleToUser(newUser.getUsername(), newUser.getRealmRoles().get(0));
                log.info("User done");
                return newUser;
            }
        } catch (Exception e) {
            log.error("Exception in user creation in Keycloak: " ,e);
        }
        log.error("Error: {} ", util.toJson(newUser));
        return newUser;
    }
    @Override
    public String getUserKeycloakId() {
        return httpServletRequest.getUserPrincipal().getName();
    }
}
