package com.doan.nhansu.admin.filter;

import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.Message;
import com.doan.nhansu.admin.exception.UnauthorizedAccessException;
import com.doan.nhansu.admin.security.UserAuthentication;
import com.doan.nhansu.admin.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Autowired
    Message message;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException,UnauthorizedAccessException {
        final String authHeader = request.getHeader("Authorization");
        final String token;
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) throw new UnauthorizedAccessException(message.getMessage("message.unauthorized"));
        //get info user from token
        UserResponse UserResponse = jwtService.getUserResponseFromToken(token);
        if (UserResponse == null) throw new UnauthorizedAccessException(message.getMessage("message.unauthorized"));
        Authentication auth = new UserAuthentication(UserResponse, UserResponse.getTicket());
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}
