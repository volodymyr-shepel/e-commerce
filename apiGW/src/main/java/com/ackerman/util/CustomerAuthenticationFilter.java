package com.ackerman.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomerAuthenticationFilter extends
        AbstractGatewayFilterFactory<CustomerAuthenticationFilter.Config> {



    @Autowired
    private RestTemplate template;

    @Autowired
    private JwtUtil jwtUtil;



    public CustomerAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            String authHeader = JwtUtil.extractAuthToken(exchange);

            jwtUtil.validateToken(authHeader);

            // Extract roles from claims
            jwtUtil.checkUserRole(authHeader,UserRole.CUSTOMER);

            return chain.filter(exchange);
        }));
    }

    public static class Config {

    }
}
