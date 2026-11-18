package com.example.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;

@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(LoggingGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Lấy thông tin Time, Method, URI theo đúng yêu cầu đề bài
        LocalDateTime time = LocalDateTime.now();
        HttpMethod method = exchange.getRequest().getMethod();
        URI uri = exchange.getRequest().getURI();
        // In log ra Console
        log.info("[GLOBAL FILTER] Time: {} | Method: {} | URI: {}", time, method, uri);
        // Chuyển tiếp request sang filter tiếp theo trong chuỗi
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {

        return -1; // Đặt thứ tự ưu tiên của filter, -1 để chạy trước các filter khác
    }
}
