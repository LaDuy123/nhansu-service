package com.doan.nhansu.admin.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.AuthenticationRequest;
import com.doan.nhansu.admin.dto.AuthenticationResponse;
import com.doan.nhansu.admin.dto.IntrospectRequest;
import com.doan.nhansu.admin.dto.IntrospectResponse;
import com.doan.nhansu.admin.service.AuthenticationService;
import com.doan.nhansu.users.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserService userService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        ApiResponse<AuthenticationResponse> response = authenticationService.authenticate(request);
        return response;
    }
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/{id}/delete")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        ApiResponse<Boolean> res = authenticationService.delete(id);
        return res;
    }
    @PostMapping("/refreshToken")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody Map<String, String> request) {
        ApiResponse<AuthenticationResponse> res = authenticationService.refreshToken(request);
        return res;
    }

}
