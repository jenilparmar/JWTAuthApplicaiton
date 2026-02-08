package com.JWTAuth.JWTAuthApplicaiton.configs;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        logger.info("========================================");
        logger.info("INCOMING REQUEST");
        logger.info("Method: {}", httpRequest.getMethod());
        logger.info("URL: {}", httpRequest.getRequestURL());
        logger.info("URI: {}", httpRequest.getRequestURI());
        logger.info("Content-Type: {}", httpRequest.getContentType());
        logger.info("========================================");

        chain.doFilter(request, response);
    }
}