package com.duo.examples.health.Normal;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @author pythias
 * @since 2019-06-06
 */
@Component
public class RedisHealthIndicator extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        Jedis jedis = new Jedis();
        if (jedis.set("hello", "world-" + System.currentTimeMillis()).equals("OK")) {
            builder.up();
        } else {
            builder.down();
        }
    }
}
