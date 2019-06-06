package com.duo.examples.health.Normal;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * @author pythias
 * @since 2019-06-06
 */
@Component
public class ApiHealthIndicator extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet httpGet = new HttpGet("https://ecs.aliyuncs.com/?Format=json");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        builder.up().withDetail("code", response.getStatusLine().getStatusCode());
    }
}
