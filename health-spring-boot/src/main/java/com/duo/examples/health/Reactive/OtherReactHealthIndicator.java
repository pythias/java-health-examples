package com.duo.examples.health.Reactive;

import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author pythias
 * @since 2019-06-05
 */
@Component
public class OtherReactHealthIndicator extends AbstractReactiveHealthIndicator {
    protected Mono<Health> doHealthCheck(Health.Builder builder) {
        return Mono.create(sink -> sink.success(this.check(builder)));
    }

    private Health check(Health.Builder builder) {
        try {
            // 各种检查
            return builder.up().build();
        } catch (Throwable e) {
            return builder.down(e).build();
        }
    }
}
