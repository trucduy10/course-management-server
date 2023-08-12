package com.aptech.coursemanagementserver.dtos.oauth;

import java.util.Map;

public class LinkedInOAuth2UserInfo extends OAuth2UserInfo {
    public LinkedInOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        String profilePictureUrl = (String) attributes
                .get("profilePicture.displayImage~.elements[0].identifiers[0].identifier");
        if (profilePictureUrl != null) {
            // Remove query params from the profile picture URL to get a smaller image
            profilePictureUrl = profilePictureUrl.split("\\?")[0];
            return profilePictureUrl;
        } else {
            return null;
        }
    }
}
