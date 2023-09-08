package com.sparta.petnexus.common.security.info.impl;


import com.sparta.petnexus.common.security.info.OAuth2UserInfo;
import java.util.Map;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {

    private Map<String, Object> response;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        this.response = (Map<String, Object>) attributes.get("response");
    }


    @Override
    public String getName() {
        return (String) response.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) response.get("email");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return response;
    }
}
