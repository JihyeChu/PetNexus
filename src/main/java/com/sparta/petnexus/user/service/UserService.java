package com.sparta.petnexus.user.service;

import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.common.redis.utils.RedisUtils;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.common.security.jwt.TokenProvider;
import com.sparta.petnexus.user.dto.AddPetRequest;
import com.sparta.petnexus.user.dto.LoginRequest;
import com.sparta.petnexus.user.dto.ProfileRequest;
import com.sparta.petnexus.user.dto.ProfileResponse;
import com.sparta.petnexus.user.dto.SignupRequest;
import com.sparta.petnexus.user.pet.entity.Pet;
import com.sparta.petnexus.user.entity.User;
import com.sparta.petnexus.user.pet.repository.PetRepository;
import com.sparta.petnexus.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final TokenProvider tokenProvider;
    private final RedisUtils redisUtils;

    public void signUp(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EXISTED_EMAIL);
        }

        String encodePassword = passwordEncoder.encode(request.getPassword());
        userRepository.save(request.toEntity(encodePassword));
    }

    public void logIn(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            LoginRequest request) {
        try {
            Authentication authentication = authenticationConfiguration.getAuthenticationManager()
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(),
                                    request.getPassword(),
                                    null
                            )
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

            String accessToken = tokenProvider.generateToken(user,
                    TokenProvider.ACCESS_TOKEN_DURATION);

            // accessToken -> cookie
            tokenProvider.addTokenToCookie(httpRequest, httpResponse, accessToken);

            // generateRefreshToken, and redis save
            String refreshToken = tokenProvider.generateRefreshToken(user,
                    TokenProvider.REFRESH_TOKEN_DURATION);

            //refreshToken -> cookie
            tokenProvider.addRefreshTokenToCookie(httpRequest, httpResponse, refreshToken);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.BAD_ID_PASSWORD);
        }
    }

    public void createNewAccessToken(HttpServletRequest request, HttpServletResponse httpResponse) {
        String authorizationHeader = tokenProvider.getTokenFromCookie(request);
        String accessToken = tokenProvider.getAccessToken(authorizationHeader);
        String email = tokenProvider.getAuthentication(accessToken).getName();
        User user = userRepository.findByEmail(email);

        String cookieRefreshToken = tokenProvider.getRefreshTokenFromCookie(request);
        String redisRefreshToken = redisUtils.get(email, String.class);

        if (cookieRefreshToken.equals(redisRefreshToken)) {
            if (!tokenProvider.validToken(redisRefreshToken)) {
                throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
        }

        String newAccessToken = tokenProvider.generateToken(user,
                TokenProvider.ACCESS_TOKEN_DURATION);

        httpResponse.addHeader(TokenProvider.HEADER_AUTHORIZATION, newAccessToken);
    }

    @Transactional
    public void updateProfile(ProfileRequest request, User user) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(
                ()-> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );
        findUser.updateUsername(request.getNickname());
    }

    public void addPet(AddPetRequest request, User user) {
        petRepository.save(Pet.builder()
                .petName(request.getPetName())
                .petType(request.getPetType())
                .petKind(request.getPetKind())
                .petGender(request.getPetGender())
                .user(user)
                .build());
    }

    public void logOut(HttpServletRequest httpRequest) {
        String authorizationHeader = tokenProvider.getTokenFromCookie(httpRequest);
        String accessToken = tokenProvider.getAccessToken(authorizationHeader);
        String email = tokenProvider.getAuthentication(accessToken).getName();
        redisUtils.delete(email);

        redisUtils.setBlackList(accessToken,"accessToken",tokenProvider.getExpiration(accessToken));
    }

    @Transactional
    public void updatePet(Long petId, AddPetRequest request, User user) {
        Pet pet = petRepository.findByIdAndUser(petId, user);
        pet.update(request);
    }

    public void deletePet(Long petId, User user) {
        petRepository.delete( petRepository.findByIdAndUser(petId, user));
    }

    public ProfileResponse findUserProfile(Long userId) {
        User user=userRepository.findById(userId).orElseThrow(
                ()->new NullPointerException("해당 사용자가 존재하지 않습니다.")
        );
        return new ProfileResponse(user);
    }
}
