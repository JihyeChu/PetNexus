package com.sparta.petnexus.common.security.info.impl;



import com.sparta.petnexus.common.security.info.OAuth2UserInfo;
import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
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
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
