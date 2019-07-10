package net.blog.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import net.blog.exception.ApplicationException;
import net.blog.model.SocialAccount;
import net.blog.service.SocialService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class GooglePlusSocialServiceImpl implements SocialService {
    private final String googlePlusClietnId;
    private final List<String > issuers;

    GooglePlusSocialServiceImpl(ServiceManager serviceManager) {
        this.googlePlusClietnId = serviceManager.getApplicationProperty("social.googleplus.clientId");
        this.issuers = Arrays.asList(new String[]{"https://accounts.google.com", "accounts.google.com"});

    }

    @Override
    public SocialAccount getSocialAccount(String authToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singleton(googlePlusClietnId))
                    .setIssuers(issuers).build();
            GoogleIdToken idToken = verifier.verify(authToken);
            if (idToken != null) {
                Payload payload = idToken.getPayload();
                return new SocialAccount(payload.getEmail(), (String) payload.get("given_name"), (String) payload.get("picture"));
            } else {
                throw new ApplicationException("Can't get account by authToken: " + authToken);
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new ApplicationException(e);
        }
    }
}
