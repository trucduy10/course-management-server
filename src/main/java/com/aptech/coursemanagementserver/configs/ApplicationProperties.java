package com.aptech.coursemanagementserver.configs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private final Security security = new Security();

    public static final class Security {
        private final OAuth2 oauth2 = new OAuth2();

        public static final class OAuth2 {
            private List<String> authorizedRedirectUris = new ArrayList<>();

            public List<String> getAuthorizedRedirectUris() {
                return authorizedRedirectUris;
            }

            public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
                this.authorizedRedirectUris = authorizedRedirectUris;
                return this;
            }
        }

        public OAuth2 getOauth2() {
            return oauth2;
        }
    }

    public Security getSecurity() {
        return security;
    }

}
