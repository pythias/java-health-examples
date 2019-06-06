package com.duo.examples.health;

import com.netflix.runtime.health.api.Health;
import com.netflix.runtime.health.api.HealthIndicator;
import com.netflix.runtime.health.api.HealthIndicatorCallback;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @author pythias
 * @since 2019-06-06
 */
@Component("netflix_redis_health")
public class RedisHealthIndicator implements HealthIndicator {
    @Override
    public void check(HealthIndicatorCallback healthIndicatorCallback) {
        Jedis jedis = new Jedis();
        if (jedis.set("hello", "world-" + System.currentTimeMillis()).equals("OK")) {
            healthIndicatorCallback.inform(Health.healthy().build());
        } else {
            healthIndicatorCallback.inform(Health.unhealthy().build());
        }
    }

    @Override
    public String getName() {
        return "redis-health";
    }
}
