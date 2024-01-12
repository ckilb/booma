package com.ckilb.booma.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class HostNameInterceptor implements HandlerInterceptor {

    private final String expectedHostName;

    HostNameInterceptor(@Value("${application.host-name}") String expectedHostName) {
        this.expectedHostName = expectedHostName;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!this.expectedHostName.equals(request.getServerName())) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("Invalid host name.");

            return false;
        }


        return true;
    }
}
