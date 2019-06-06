package com.duo.examples.health.Reactive;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @author pythias
 * @since 2019-06-05
 */
@Component
public class ApiReactHealthIndicator extends AbstractReactiveHealthIndicator {
    protected Mono<Health> doHealthCheck(Health.Builder builder) {
        return Mono.create(sink -> sink.success(this.check(builder)));
    }

    private Health check(Health.Builder builder) {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet httpGet = new HttpGet("https://ecs.aliyuncs.com/?Format=json");
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            return builder.up().withDetail("code", response.getStatusLine().getStatusCode()).build();
        } catch (IOException e) {
            return builder.down(e).build();
        }
    }
}
