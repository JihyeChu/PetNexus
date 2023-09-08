package com.sparta.petnexus.common.security.oauth;



import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.common.security.info.OAuth2UserInfo;
import com.sparta.petnexus.common.security.info.OAuth2UserInfoFactory;
import com.sparta.petnexus.common.security.info.ProviderType;
import com.sparta.petnexus.user.entity.User;
import com.sparta.petnexus.user.entity.UserRoleEnum;
import com.sparta.petnexus.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // ❶ 요청을 바탕으로 유저 정보를 담은 객체 반환
        try {
            return this.process(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }

    }

    // ❷ 유저가 있으면 업데이트, 없으면 유저 생성
    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        ProviderType providerType = ProviderType.valueOf(
                userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType,
                oAuth2User.getAttributes());

        User savedUser = userRepository.findByEmail(userInfo.getEmail());

        if (savedUser != null) {
            if (providerType != savedUser.getProviderType()) {
                throw new IllegalArgumentException();
            }
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, providerType);
        }

        return UserDetailsImpl.create(savedUser, oAuth2User.getAttributes());
    }

    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {

        User user = new User(
                userInfo.getEmail(),
                userInfo.getName(),
                "유저",
                providerType,
                UserRoleEnum.USER
        );

        return userRepository.saveAndFlush(user);
    }

    private User updateUser(User user, OAuth2UserInfo userInfo) {
        if (userInfo.getEmail() != null && !user.getEmail().equals(userInfo.getEmail())) {
            user.updateEmail(userInfo.getEmail());
        }

        return user;
    }

}
