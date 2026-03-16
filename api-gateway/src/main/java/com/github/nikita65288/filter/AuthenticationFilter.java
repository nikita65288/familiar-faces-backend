package com.github.nikita65288.filter;

import com.github.nikita65288.jwt.JwtProvider;
import com.github.nikita65288.util.RouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthenticationFilter(
            RouteValidator validator,
            JwtProvider jwtProvider
    ) {
        super(Config.class);
        this.validator = validator;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {

                // Check headers
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
                }

                // TODO: check this line
                String tokenHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {

                    String token = tokenHeader.substring(7);

                    // Validate token
                    try {
                        jwtProvider.validateToken(token);

                        // Extract user credential ID
                        String userId = jwtProvider.getSubject(token);

                        // Modify request with new header
                        ServerHttpRequest modifiedRequest = exchange.getRequest()
                                .mutate()
                                .header("X-User-Id", userId)
                                .build();

                        // Pass the modified exchange further along the chain
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token");
                    }
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {}
}