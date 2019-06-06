package com.duo.examples.health;

import com.netflix.runtime.health.api.Health;
import com.netflix.runtime.health.api.HealthIndicator;
import com.netflix.runtime.health.api.HealthIndicatorCallback;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author pythias
 * @since 2019-06-06
 */
@Component("netflix_api_health")
public class ApiHealthIndicator implements HealthIndicator {
    @Override
    public void check(HealthIndicatorCallback healthIndicatorCallback) {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet httpGet = new HttpGet("https://ecs.aliyuncs.com/?Format=json");
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            healthIndicatorCallback.inform(Health.healthy().withDetail("code", response.getStatusLine().getStatusCode()).build());
        } catch (IOException e) {
            e.printStackTrace();
            healthIndicatorCallback.inform(Health.unhealthy(e).build());
        }
    }

    @Override
    public String getName() {
        return "xx-api-health";
    }
}
