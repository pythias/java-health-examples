package com.duo.examples.health.Reactive;

import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import redis.clients.jedis.Jedis;

/**
 * @author pythias
 * @since 2019-06-06
 */
@Component
public class RedisReactHealthIndicator extends AbstractReactiveHealthIndicator {
    @Override
    protected Mono<Health> doHealthCheck(Health.Builder builder) {
        return Mono.create(sink -> sink.success(this.check(builder)));
    }

    private Health check(Health.Builder builder) {
        Jedis jedis = new Jedis();
        if (jedis.set("hello", "world-" + System.currentTimeMillis()).equals("OK")) {
            return builder.up().build();
        }

        return builder.down().build();
    }
}
