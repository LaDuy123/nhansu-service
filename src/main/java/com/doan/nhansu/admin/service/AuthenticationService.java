package com.doan.nhansu.admin.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.*;
import com.doan.nhansu.admin.entity.RefreshTokenEntity;
import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.repository.RefreshTokenRepository;
import com.doan.nhansu.admin.repository.RefreshTokenRepositoryCustom;
import com.doan.nhansu.users.entity.UserEntity;
import com.doan.nhansu.users.repository.UserRepository;
import com.doan.nhansu.users.repository.custom.UserRepositoryCustom;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
//    @Value("${jwt.expirationTimeMinutes}")
//    private String EXPIRATION_TIME_IN_MINUTES;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    HttpServletRequest request;
    UserRepositoryCustom userRepositoryCustom;
    JwtService jwtService;
    RefreshTokenRepository refreshTokenRepository;
    RefreshTokenRepositoryCustom refreshTokenRepositoryCustom;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verify = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(verify && expiredTime.after(new Date()))
                .build();

    }
    public ApiResponse<AuthenticationResponse> refreshToken(Map<String, String> request){
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        String refreshToken = request.get("refreshToken");
        RefreshTokenDTO refreshTokenDTO = refreshTokenRepositoryCustom.getRefreshToken(refreshToken)
                .orElseThrow(() -> new AppException("Giá trị Refresh Token không đúng"));

        if (refreshTokenDTO.getExpiryDate().before(new Date())) {
            refreshTokenRepositoryCustom.deleteRefreshToken(refreshTokenDTO.getUserId());
            throw new RuntimeException("Refresh Token expired");
        }
        String userName = jwtService.extractUsername(refreshToken);
        UserResponse user = userRepositoryCustom.finByUserNameResponse(userName).orElseThrow(() -> new AppException(MessageError.USER_NOT_EXISTED));
        String newAccessToken = jwtService.generateToken(user);
        authenticationResponse.setAccessToken(newAccessToken);
        authenticationResponse.setUser(user);
        return new ApiResponse.ResponseBuilder<AuthenticationResponse>().success(authenticationResponse);
    }

    public ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest request){
        AuthenticationResponse response = new AuthenticationResponse();
        UserResponse user = userRepositoryCustom.finByUserNameResponse(request.getUsername()).orElseThrow(() ->
                new AppException("Tài khoản không tồn tại"));
        boolean authenticate = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticate)
            throw new AppException(MessageError.INCORRECT_PASSWORD);
        refreshTokenRepository.deleteByUserId(user.getId());
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUserId(user.getId());
        long currentTimeMillis = System.currentTimeMillis();
        Date expiryDate = new Date(currentTimeMillis + jwtService.REFRESH_TOKEN_VALIDITY);
        refreshTokenEntity.setExpiryDate(expiryDate);

        refreshTokenRepository.save(refreshTokenEntity);
        response.setAccessToken(token);
        response.setAuthenticated(true);
        response.setUser(user);
        response.setRefreshToken(refreshToken);
        user.setPassword(null);
        return new ApiResponse.ResponseBuilder<AuthenticationResponse>().success(response, "Đăng nhập thành công");
    }
    public ApiResponse<Boolean> delete(Long id) {
        if (Objects.nonNull(id)){
            refreshTokenRepositoryCustom.deleteRefreshToken(id);
            return new ApiResponse.ResponseBuilder<Boolean>().success(true);
        }
        return new ApiResponse.ResponseBuilder<Boolean>().failed(null, MessageError.NOT_EXISTED);
    }

}
