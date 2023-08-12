package com.aptech.coursemanagementserver.repositories;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;
import static com.aptech.coursemanagementserver.constants.GlobalStorage.REDIRECT_URI_PARAM_COOKIE_NAME;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import com.aptech.coursemanagementserver.utils.CookieUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final int cookieExpireSeconds = 180;

    @Override
    // Retrieve the Authorization Request that was previously stored in a temporary
    // storage location
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request,
                OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie,
                        OAuth2AuthorizationRequest.class))
                .orElse(null);

        // return CookieUtils.getCookie(request,
        // OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        // .map(cookie -> {
        // try {
        // return CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // return null;
        // })
        // .orElse(null);
    }

    @Override
    // Store the Authorization Request in a temporary location
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
            HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            return;
        }

        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }

    // Remove the Authorization Request from the temporary location once it has been
    // used, or if it expires or is no longer needed
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
            HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }
}
